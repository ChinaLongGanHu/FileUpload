package com.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

@Controller
@RequestMapping({ "/springService" })
public class UploadAction {

	@RequestMapping({ "/upload" })
	@ResponseBody
	public boolean upload(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		if (request instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest multiRequset = (DefaultMultipartHttpServletRequest) request;
			for (Entry<String, MultipartFile> e : multiRequset.getFileMap()
					.entrySet()) {
				MultipartFile file = e.getValue();
				File tempFile = new File("C:/Users/祺/Desktop/" + file.getOriginalFilename());
				FileOutputStream fos = null;
				InputStream fis = null;
				byte[] b = new byte[4096];
				int len;
				try {
					fos = new FileOutputStream(tempFile);
					fis = file.getInputStream();
					while ((len = fis.read(b)) != -1) {
						fos.write(b, 0, len);
					}

				} catch (Exception e2) {
					e2.printStackTrace();
					return false;
				} finally {
					try {
						fos.close();
					} catch (Exception e3) {
						try {
							fos = null;
						} catch (Exception e1) {
						}
					}

					try {
						fis.close();
					} catch (Exception e3) {
						try {
							fis = null;
						} catch (Exception e1) {
						}
					}

				}

			}
		}
		return true;
	}

	/**
	 * 
	 * <p>
	 * 作用描述：生成验证码
	 * </p>
	 * <p>
	 * 修改说明：
	 * </p>
	 * 
	 * @throws IOException
	 * 
	 */
	@RequestMapping({ "/genVerfCode" })
	@ResponseBody
	public void genVerfCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		HttpSession session = request.getSession();

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		int width = 60, height = 22;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

		// 字母数字验证码
		char c[] = new char[10];

		for (int m = 48, n = 0; m < 58; m++, n++) {
			c[n] = (char) m;
		}
		String verfCoe = "";
		for (int i = 0; i < 4; i++) {
			int x = random.nextInt(10);
			String rand = String.valueOf(c[x]);
			verfCoe += rand;

			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		// 将认证码存入SESSION
		session.setAttribute("verfCoe", verfCoe);

		g.dispose();

		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.getOutputStream().close();

	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 
	 * <p>
	 * 作用描述：获取验证码
	 * </p>
	 * <p>
	 * 修改说明：
	 * </p>
	 * 
	 * @return
	 * 
	 */
	@RequestMapping({ "/getVerfCode" })
	@ResponseBody
	public String getVerfCode(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("verfCoe");
	}
}
