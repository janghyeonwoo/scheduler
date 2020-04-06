package com.spring.scheduler.team.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.scheduler.team.dto.TeamDTO;
import com.spring.scheduler.team.dto.ThumbnailDTO;


@Repository
public class TeamDAOImpl implements TeamDAO {
	private static final String NS = "team."; //네임스페이스
	private static final String INSERTTEAM = "insertteam"; //팀등록
	private static final String INSERTMEMBER = "insertmember";  //멤버등록
	private static final String INSERTTHUMBNAIL = "insertthumbnail"; //팀 썸네일 등록
	private static final String TEAMLIST = "teamlist"; //팀리스트 가져오기
	private static final String SELECTTHUMBNAIL = "selectthumbnail"; //팀썸네일 가져오기
	@Inject
	SqlSession sqlSession;
	
	
	//팀등록
	@Override
	public int insertTeam(TeamDTO team) {
		
		
		boolean check = sqlSession.insert(INSERTTEAM,team) == 1? true :false;  // 등록 성공 
		
		if(check == true) { // 등록실패
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tbno",team.getTbno());
			map.put("uid",team.getTeamMaster());
			map.put("authority","master");
			
			sqlSession.insert(INSERTMEMBER,map);
			
		}
		
				
		
		
		
		
		return team.getTbno();
		
		

	}
	//팀 썸네일 등록
	@Override
	public void intsertThumbnail(List<ThumbnailDTO> thumbnail, int tbno) {
		thumbnail.forEach(thum -> {
			thum.setTbno(tbno);
			sqlSession.insert(INSERTTHUMBNAIL,thum);
			});
		
		
	}
	@Override
	public List<TeamDTO> getList() {
		List<TeamDTO> teamList = sqlSession.selectList(TEAMLIST);
		
		teamList.forEach(team -> {
			
			int tbno = team.getTbno();
			List<ThumbnailDTO> thumbnailList = sqlSession.selectList(SELECTTHUMBNAIL,tbno);
			
			
			
			if(!thumbnailList.isEmpty()) {
				
				team.setThumbnailList(thumbnailList);
			}
			
			
			
		});
		
		System.out.println("aa"+ teamList.get(1).getThumbnailList().get(0).getMainThumbnail());
		System.out.println("aa"+ teamList.get(1).getThumbnailList().get(1).getBackThumbnail());
		return teamList;
	}
	@Override
	public List<ThumbnailDTO> getThumnail(int tIdx) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
