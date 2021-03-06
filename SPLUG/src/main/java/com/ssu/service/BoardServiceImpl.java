package com.ssu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssu.domain.BoardVO;
import com.ssu.persistence.BoardDAO;
import com.ssu.util.FileUtils;

@Service("BoardService")
public class BoardServiceImpl implements BoardService {

	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	@Autowired
	private FileUtils fileUtils;

	@Inject
	BoardDAO boardDao;

	// 01. 게시글쓰기
	@Override
	public void create(BoardVO vo, String where) throws Exception {
		String title = vo.getTitle();
		String content = vo.getContent();
		String writer = vo.getWriter();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");

		// *태그문자 처리 (< ==> &lt; > ==> &gt;)
		// replace(A, B) A를 B로 변경
		title = title.replace("<", "&lt;");
		title = title.replace("<", "&gt;");
		writer = writer.replace("<", "&lt;");
		writer = writer.replace("<", "&gt;");
		// *공백문자 처리
		title = title.replace("  ", "&nbsp;&nbsp;");
		writer = writer.replace("  ", "&nbsp;&nbsp;");
		// *줄바꿈 문자처리
		content = content.replace("\n", "<br>");
		vo.setTitle(title);
		vo.setContent(content);
		vo.setWriter(writer);
		vo.setRegdate(format.format(date));

		boardDao.create(vo, where);
	}

	@Override
	public void create(BoardVO vo, String where, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		String title = vo.getTitle();
		String content = vo.getContent();
		String writer = vo.getWriter();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		// MultipartHttpServletRequest multipartHttpServletRequest =
		// (MultipartHttpServletRequest) request;
		// Iterator<String> iterator =
		// multipartHttpServletRequest.getFileNames();
		// MultipartFile multipartFile = null;

		// *태그문자 처리 (< ==> &lt; > ==> &gt;)
		// replace(A, B) A를 B로 변경
		title = title.replace("<", "&lt;");
		title = title.replace("<", "&gt;");
		writer = writer.replace("<", "&lt;");
		writer = writer.replace("<", "&gt;");
		// *공백문자 처리
		title = title.replace("  ", "&nbsp;&nbsp;");
		writer = writer.replace("  ", "&nbsp;&nbsp;");
		// *줄바꿈 문자처리
		content = content.replace("\n", "<br>");
		vo.setTitle(title);
		vo.setContent(content);
		vo.setWriter(writer);
		vo.setRegdate(format.format(date));

		// 파일 업로드 리스트
		// while (iterator.hasNext()) {
		// multipartFile = multipartHttpServletRequest.getFile(iterator.next());
		// if (multipartFile.isEmpty() == false) {
		// System.out.println("name : " + multipartFile.getName());
		// System.out.println("filename : " +
		// multipartFile.getOriginalFilename());
		// System.out.println("size : " + multipartFile.getSize());
		// }
		// }
		boardDao.create(vo, where);
		
		where="data_upload";
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(vo, request);
		for (int i = 0, size = list.size(); i < size; i++) {
			boardDao.create(list.get(i), where);
		}

	}

	// 02. 게시글 상세보기
	@Override
	public BoardVO read(int bno, String where) throws Exception {
		return boardDao.read(bno, where);
	}
	
	@Override
	public Map<String, Object> read(int bno) throws Exception {
		// TODO Auto-generated method stub
		return boardDao.read(bno);
	}
	@Override
	public Map<String, Object> read_file_info(int bno) throws Exception {
		// TODO Auto-generated method stub
		return boardDao.read_file_info(bno);
	}

	// 03. 게시글 수정
	@Override
	public void update(BoardVO vo, String where) throws Exception {
		boardDao.update(vo, where);
	}

	// 04. 게시글 삭제
	@Override
	public void delete(int bno, String where) throws Exception {
		boardDao.delete(bno, where);
	}

	// 05. 게시글 전체 목록
	@Override
	public List<BoardVO> listAll(int start, int end, String where) throws Exception {

		return boardDao.listAll(start, end, where);
	}

	// 06. 게시글 조회수 증가
	@Override
	public void increaseViewcnt(int bno, HttpSession session, String where) throws Exception {
		long update_time = 0;
		// 세션에 저장된 조회시간 검색
		// 최초로 조회할 경우 세션에 저장된 값이 없기 때문에 if문은 실행X
		if (session.getAttribute("update_time_" + bno) != null) {
			// 세션에서 읽어오기
			update_time = (Long) session.getAttribute("update_time_" + bno);
		}
		// 시스템의 현재시간을 current_time에 저장
		long current_time = System.currentTimeMillis();
		// 일정시간이 경과 후 조회수 증가 처리 24*60*60*1000(24시간)
		// 시스템현재시간 - 열람시간 > 일정시간(조회수 증가가 가능하도록 지정한 시간)
		if (current_time - update_time > 5 * 1000) {
			boardDao.increaseViewcnt(bno, where);
			// 세션에 시간을 저장 : "update_time_"+bno는 다른변수와 중복되지 않게 명명한 것
			session.setAttribute("update_time_" + bno, current_time);

		}
	}

	// 07. 전체 게시글 수 조회
	@Override
	public int countArticle(String where) {

		return boardDao.countArticle(where);

	}

	

}
