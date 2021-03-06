package com.spring.scheduler.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.scheduler.team.dto.CalendarDTO;
import com.spring.scheduler.team.dto.TeamDTO;
import com.spring.scheduler.team.service.TeamService;
import com.spring.scheduler.user.dto.UserDTO;
import com.spring.scheduler.user.interceptor.SessionAttr;

@Controller
@RequestMapping("/team/*")
public class TeamController {
	
	
	@Inject
	TeamService teamService;
	//팀등록 페이지 이동 
	@RequestMapping(value="/register" , method = RequestMethod.GET)
	public void teamRegister() {
		
	}
	//팀등록 
	@RequestMapping(value="/register" , method = RequestMethod.POST)
	public String teamRegisteAction(TeamDTO team, HttpSession session) {
		UserDTO user  = (UserDTO)session.getAttribute(SessionAttr.LOGINUSER);
		team.setTeamMaster(user.getUid());
			
			
		teamService.insertTeam(team);
		
		
		
		return "list";
		
	}
	//팀리스트보여주기
	@RequestMapping(value="/teamlist", method=RequestMethod.GET)
	public void teamlist(Model model) {
		
		
		 
		/*
		 * List<TeamDTO> teamlist = teamService.getList(); System.out.println(teamlist);
		 * model.addAttribute("list", teamlist);
		 */
		
		
		
	}

	//팀 상세 정보 보기 
	@RequestMapping(value="/teamdetail" , method=RequestMethod.GET)
	public void teamdetail (@RequestParam int tbno, Model model ,HttpSession session) {
		UserDTO user = (UserDTO)session.getAttribute(SessionAttr.LOGINUSER);
		Map<String, Object> map = new HashMap<String, Object>();
		
		//초대 신청을 한번이라도 했던가 아니면 이미 팀에 등록된 회원인지 확인하여 teamdetail의 버튼을 disabled 한다 . 
		if(user != null) {
			String uid = user.getUid();
			
			map.put("tbno", tbno);
			map.put("uid", uid);
			
			String msg = teamService.getCheckApply(map);
			
			if(msg != null) {
				model.addAttribute("msg", msg);
				System.out.println(msg);
			}
			
		}
		TeamDTO team = teamService.getTeamDetail(tbno);
		model.addAttribute("team", team);
		model.addAttribute("tbno", tbno);
		
	}
	
	
	//무한스크롤 요청 
	@ResponseBody
	@RequestMapping(value="/teamlist", method = RequestMethod.POST)
	public List<TeamDTO> teamlist(@RequestBody Map<String,Integer> map) {
		 
		
		return teamService.getList(map);
		
		
	}
	//팀참여 신청 하기 (로그인 하지않은 경우 AuthInterceptor에 의해 걸러짐)
	@RequestMapping(value="/teamapplication" , method = RequestMethod.GET)
	public String teamapplication(@RequestParam("tbno") int tbno , RedirectAttributes re  ,HttpSession session) {
		
		UserDTO user = (UserDTO) session.getAttribute(SessionAttr.LOGINUSER);	
		String uid = user.getUid();
	
		teamService.insertTeamApply(tbno,uid); 
		re.addAttribute("tbno", tbno);
		re.addFlashAttribute("apply_result","신청이 완료되었습니다");
		
		
		return "redirect:/team/teamdetail";
		
		
	}
	//myteam 페이지 이동 
	@RequestMapping(value="myteam" , method=RequestMethod.GET)
	public void myteam(HttpSession session ,Model model) {
		UserDTO user =(UserDTO)session.getAttribute(SessionAttr.LOGINUSER);
		
		//참여대기중인 팀 리스트 보여주기 
		List<TeamDTO> list = teamService.getWaitingTeamList(user.getUid());
		model.addAttribute("list",list); 

		
		
	}
	
	//myteam에서 tab에 해당하는 teamlist 가져오기 ex)참여중인 팀, 개설한 팀 , 찜한팀 
	@ResponseBody
	@RequestMapping("myteam/{teamlistgroup}")
	public List<TeamDTO> myteamlist(@PathVariable String teamlistgroup, HttpSession session){
		UserDTO user = (UserDTO)session.getAttribute(SessionAttr.LOGINUSER);
		String uid = user.getUid();
		Map<String,String> map = new HashMap<String, String>();
		map.put("uid" , uid) ;
		map.put("teamlistgroup" , teamlistgroup) ;
		List<TeamDTO> list = teamService.getTeamListGroup(map);
		
		return list;
	}
	
	@RequestMapping(value="myteam/teamcalendar/{tbno}" , method=RequestMethod.GET)
	public String teamcalendar(@PathVariable String tbno , HttpSession session) {
		
		return "team/teamcalendar";
	}
	
	@ResponseBody
	@RequestMapping(value="calendar/{tbno}" , method=RequestMethod.GET)
public JSONArray getCalendar(@PathVariable String tbno) {
		
		
	
		
		JSONObject ob = new JSONObject();
		ob.put("id", 1);
		ob.put("title", "거래처 미팅");
		ob.put("description","Lorem ipsum dolor sit incid idunt ut Lorem ipsum sit.");
		ob.put("start", "2019-05-07T09:30");
		ob.put("end", "2019-05-07T15:00");
		ob.put("type", "카테고리1");
		ob.put("username", "다현");
		ob.put("backgroundColor", "#D25565");
		ob.put("textColor", "#ffffff");
		ob.put("allDay", false);
		
		
		JSONObject ob2 = new JSONObject();
		ob2.put("id", 2);
		ob2.put("title", "거래처 미팅");
		ob2.put("description","Lorem ipsum dolor sit incid idunt ut Lorem ipsum sit.");
		ob2.put("start", "2019-05-14T09:30");
		ob2.put("end", "2019-05-16T15:00");
		ob2.put("type", "카테고리1");
		ob2.put("username", "다현");
		ob2.put("backgroundColor", "#D25565");
		ob2.put("textColor", "#ffffff");
		ob2.put("allDay", false);
		
		JSONArray array = new JSONArray();
		array.add(ob);
		array.add(ob2);
		
		
		
		
		return array;
	}
	
	 
	
	@ResponseBody
	@RequestMapping(value = "calendar/{tbno}", method = RequestMethod.POST)
	public String setCalendar(@PathVariable String tbno,  @RequestBody CalendarDTO dto) {
		System.out.println(dto);
		return "완료"; 

	}	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

