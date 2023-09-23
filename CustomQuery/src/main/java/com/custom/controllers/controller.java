package com.custom.controllers;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.custom.enties.VideoRequest;
import com.custom.enties.img;
import com.custom.enties.rites;
import com.custom.respository.RespoVideo;
import com.custom.respository.imgRespo;
import com.custom.respository.respo;

import jakarta.servlet.http.HttpSession;

@Controller
public class controller {

	@Autowired
	respo respo;
	
	@Autowired
	RespoVideo respoVideo;

	@Autowired
	imgRespo imgRespo;

	
	
	
	@RequestMapping("/submit")
	public String submits(@ModelAttribute VideoRequest op) {
		
		respoVideo.save(op);
		return "as";
	}
	
	
	@RequestMapping("/insert")
	public String register(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phoneNo") String phoneNo, @RequestParam("password") String password) {
		rites rites = new rites();

		rites.setName(name);
		rites.setEmail(email);
		rites.setPhoneNo(phoneNo);
		rites.setPassword(password);
		rites.setAdmin(false);
		respo.save(rites);
		return "index";
	}
	
	

	@RequestMapping("/addAdmin")
	public String addAdmin(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phoneNo") String phoneNo, @RequestParam("password") String password) {
		rites rites = new rites();

		rites.setName(name);
		rites.setEmail(email);
		rites.setPhoneNo(phoneNo);
		rites.setPassword(password);
		rites.setAdmin(true);

		respo.save(rites);
		return "index";
	}

	@RequestMapping(value = "/upload")
	public void videoSave(@RequestParam("thumbnail") MultipartFile thumbnail,
			@RequestParam("video") MultipartFile video, @RequestParam("name") String name, HttpSession s) {

		System.out.println(thumbnail.getOriginalFilename());
		String thumbnails = thumbnail.getOriginalFilename();
		String videos = video.getOriginalFilename();

		img img = new img();
		img.setThumbnails(thumbnails);
		img.setName(name);
		img.setVideos(videos);

		imgRespo.save(img);

		try {
			// video
			byte[] b = thumbnail.getBytes();
			String path = s.getServletContext().getRealPath("images/") + thumbnail.getOriginalFilename();
			System.out.println(path);
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(b);
			fos.close();

			// images
			byte[] c = video.getBytes();
			String paths = s.getServletContext().getRealPath("video/") + video.getOriginalFilename();

			System.out.println(paths);
			FileOutputStream foss = new FileOutputStream(paths);
			foss.write(c);
			foss.close();

		} catch (Exception e) {

			// TODO: handle exception
		}
	}

	@RequestMapping("login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String pass) {

		rites ses = respo.findByLogin(email, pass);

		if (ses != null) {

			if (ses.getAdmin() == true) {
				return "redirect:/show";
			}

			return "redirect:/data";
		}

		return "index";
	}

	@RequestMapping("data")
	public String showpages(Model m) {

		ArrayList<img> ops = (ArrayList<img>) imgRespo.findAll();

		m.addAttribute("obj", ops);
		return "viewDate";
	}

}
