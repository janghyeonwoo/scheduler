package com.spring.scheduler.team.service;

import java.util.List;
import java.util.Map;

import com.spring.scheduler.team.dto.TeamDTO;
import com.spring.scheduler.team.dto.ThumbnailDTO;

public interface TeamService {
	
	//팀등록하기 
	public void insertTeam(TeamDTO team); 

	// 팀리스트 가져오기
	public List<TeamDTO> getList(Map<String,Integer> map);

	// 팀리스트 가져오기
	public List<ThumbnailDTO> getThumnail(int tbno);

}
