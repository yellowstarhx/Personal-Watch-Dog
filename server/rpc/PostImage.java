package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Image;
import entity.Image.ImageBuilder;
import external.TelegramBotAPI;

/**
 * Servlet implementation class PostImage
 */
@WebServlet("/post")
public class PostImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static TelegramBotAPI bot = new TelegramBotAPI();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
		String res = "test";
//		out.println("Hello");
		
		try {
			JSONObject input = RpcHelper.readJSONObject(request);

			ImageBuilder builder = new ImageBuilder();			
			builder.setFilename(input.getString("filename"));
			builder.setCamera_id(input.getString("camera_id"));
			builder.setCapture_time(input.getString("capture_time"));
			builder.setMLResult(input.getString("mlResult"));
			builder.setUrl(input.getString("url"));;
			builder.setComment(input.getString("comment"));
			Image image = builder.build();
//			out.println(image.getUrl());
			DBConnection conn = DBConnectionFactory.getConnection();
			if (conn.saveImage(image)) {
				res = "Successed: Save image " + input.getString("filename") + "to DB.";
				
				// send msg & image to telegram
				bot.sendToTelegram(image);
				
			} else {
				res = "Failed.";
			}
			RpcHelper.writeJsonObject(response, new JSONObject().put("Result", res));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
