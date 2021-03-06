package admincontroller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import DAO.AlbumDAO;
import DAO.CountryDAO;
import DAO.LyricDAO;
import DAO.SingerDAO;
import DAO.SongDAO;
import entity.Album;
import entity.Country;
import entity.Lyric;
import entity.Singer;
import entity.Song;
import entity.Users;
import processor.SlugProcessor;

/**
 * Servlet implementation class SongUpdateController
 */
@WebServlet("/SongUpdateController")
public class SongUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "music";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 10;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 20;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 40;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SongUpdateController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		int page = 1;
		if (request.getAttribute("currentPage") != null)
			page = (Integer) request.getAttribute("currentPage");
		request.setAttribute("page", page);
		request.setAttribute("currentPage", page);
		if (!ServletFileUpload.isMultipartContent(request)) {
			request.setAttribute("error", "Error: Form must has enctype=multipart/form-data.");
			request.setAttribute("link", "admin/update/song/" + SlugProcessor.getParam(request.getRequestURI()));
			request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
			;
		}

		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// sets memory threshold - beyond which files are stored in disk
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// sets temporary location to store files
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);

		// sets maximum size of upload file
		upload.setFileSizeMax(MAX_FILE_SIZE);

		// sets maximum size of request (include file + form data)
		upload.setSizeMax(MAX_REQUEST_SIZE);
		upload.setHeaderEncoding(request.getCharacterEncoding());

		try {
			// parses the request's content to extract file data
			List<FileItem> items = upload.parseRequest(request);

			Iterator iter = items.iterator();
			Hashtable param = new Hashtable<>();
			String linkSong = "";
			if (items != null && items.size() > 0) {
				// iterates over form's fields
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					// processes only fields that are not form fields
					if (!item.isFormField()) {
						// constructs the directory path to store upload
						// file
						// this path is relative to application's directory
						String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

						// creates the directory if it does not exist
						File uploadDir = new File(uploadPath);
						if (!uploadDir.exists()) {
							uploadDir.mkdir();
						}
						if (item.getName() != null && !"".equals(item.getName())) {
							String fileName = new File(item.getName()).getName();
							if (fileName != null) {
								String filePath = uploadPath + File.separator + fileName;
								File storeFile = new File(filePath);
								// saves the file on disk
								item.write(storeFile);
								linkSong = "/music/" + fileName;
							}
						}
					} else if (item.isFormField()) {
						param.put(item.getFieldName(), item.getString("UTF-8"));
					}
				}
			}
			int id = Integer.parseInt((String) param.get("idSong"));
			Song oldSong = SongDAO.getSong(id);
			String nameSong = (String) param.get("name_song");
			if (nameSong == null || "".equals(nameSong))
				nameSong = oldSong.getNameSong();
			String keyWord = TextFormat.createKeyWord(nameSong);
			String slugSong = TextFormat.createSlug(keyWord, id);
			String al = (String) param.get("album");
			Album album = null;
			if (!"none".equalsIgnoreCase(al) && al != null && !"".equals(al))
				album = AlbumDAO.getAlbumByID(Integer.parseInt(al));
			String sig = (String) param.get("singers");
			Country country = CountryDAO.getCountryBySlug((String) param.get("country"));

			List<Singer> listSinger;
			if (sig == null || "".equals(sig))
				listSinger = oldSong.getSingers();
			else {
				StringTokenizer st = new StringTokenizer(sig, "|");
				listSinger = SingerDAO.createListSinger(st);
			}
			Date postDate = oldSong.getPostDate();
			Integer viewcount = oldSong.getViewcount();
			Integer likes = oldSong.getLikes();
			if ("".equals(linkSong.trim()))
				linkSong = oldSong.getLinkSong();
			List<Users> userses = oldSong.getUserses();
			Song song = new Song(id, album, country, nameSong, keyWord, slugSong, postDate, viewcount, likes, linkSong,
					listSinger, userses, oldSong.getLyric(), oldSong.getCommentses(), oldSong.getPlaylists());
			if (SongDAO.update(song)) {
				String a = (String) param.get("lyric");
				Lyric lyric = new Lyric(song, a);
				if (LyricDAO.insert(lyric)) {
					request.getRequestDispatcher("/admin/danhsachbaihat/" + page).forward(request, response);
					return;
				} else {
					request.setAttribute("error", "Insert lyric false");
					request.setAttribute("link",
							"admin/update/song/" + SlugProcessor.getParam(request.getRequestURI()));
					request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("error", "Insert false");
				request.setAttribute("link", "admin/update/song/" + SlugProcessor.getParam(request.getRequestURI()));
				request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			request.setAttribute("link", "admin/update/song/" + SlugProcessor.getParam(request.getRequestURI()));
			request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
			;
		}
	}

}
