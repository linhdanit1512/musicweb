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
import DAO.CategoryDAO;
import DAO.CountryDAO;
import DAO.SingerDAO;
import DAO.SongDAO;
import entity.Album;
import entity.Category;
import entity.Country;
import entity.Singer;
import entity.Song;
import processor.SlugProcessor;

/**
 * Servlet implementation class AlbumUpdateController
 */
@WebServlet("/AlbumUpdateController")
public class AlbumUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "img/album";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 2;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 3;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 4;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlbumUpdateController() {
		super();
		// TODO Auto-generated constructor stub
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
			request.setAttribute("link", "admin/danhsachalbum/" + page);
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

		// constructs the directory path to store upload file
		// this path is relative to application's directory
		String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

		// creates the directory if it does not exist

		try {
			// parses the request's content to extract file data
			List<FileItem> items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			Hashtable param = new Hashtable<>();
			String linkImg = "";
			if (items != null && items.size() > 0) {
				// iterates over form's fields
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					// processes only fields that are not form fields
					if (!item.isFormField()) {
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
								linkImg = "/img/album/" + fileName;
							}
						}
					} else if (item.isFormField()) {
						param.put(item.getFieldName(), item.getString("UTF-8"));
					}
				}
			}
			int id = Integer.parseInt((String) param.get("idAlbum"));
			Album oldAlbum = AlbumDAO.getAlbumByID(id);
			System.out.println("old: " + oldAlbum.toString());
			String nameAlbum = (String) param.get("nameAlbum");
			if (nameAlbum == null || "".equals(nameAlbum))
				nameAlbum = oldAlbum.getNameAlbum();
			String keyWord = TextFormat.createKeyWord(nameAlbum);
			String slugAlbum = TextFormat.createSlug(keyWord, id);
			Category category = CategoryDAO.getCategoryByID(Integer.parseInt((String) param.get("category")));
			Country country = CountryDAO.getCountryByName((String) param.get("country"));
			String infoAlbum = (String) param.get("infoAlbum");
			Date postDate = oldAlbum.getPostDate();

			String sing = ((String) param.get("singers")).trim();
			StringTokenizer st = new StringTokenizer(sing, "|");
			List<Singer> listSinger = SingerDAO.createListSinger(st);

			String song = ((String) param.get("songs")).trim();
			StringTokenizer st2 = new StringTokenizer(song, "|");
			List<Song> listSong = SongDAO.createListSong(st2);
			if (linkImg == null || "".equals(linkImg)) {
				if (oldAlbum.getLinkImg() != null && !"".equals(oldAlbum.getLinkImg()))
					linkImg = oldAlbum.getLinkImg();
				else
					linkImg = "/img/admin/no-images.png";
			}
			Album album = new Album(id, category, country, nameAlbum, keyWord, slugAlbum, postDate, 0, linkImg,
					infoAlbum, null, listSinger, null, listSong);
			System.out.println(album.toString());

			if (AlbumDAO.update(album)) {
				request.getRequestDispatcher("/admin/danhsachalbum/" + page).forward(request, response);
				return;
			} else {
				request.setAttribute("error", "Update false");
				request.setAttribute("link", "/admin/danhsachalbum/" + page);
				request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			request.setAttribute("link", "admin/update/album/" + SlugProcessor.getParam(request.getRequestURI()));
			request.getRequestDispatcher("/admin/errorpage.jsp").forward(request, response);
		}
	}

}
