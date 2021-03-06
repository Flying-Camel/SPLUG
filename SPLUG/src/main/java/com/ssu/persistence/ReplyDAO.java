package com.ssu.persistence;

import java.util.List;
import com.ssu.domain.ReplyVO;
import org.mybatis.spring.annotation.MapperScan;



public interface ReplyDAO {
    // 댓글 목록
    public List<ReplyVO> list(Integer bno,String where);
    // 댓글 입력
    public void create(ReplyVO vo,String where);
    // 댓글 수정
    public void update(ReplyVO vo);
    // 댓글 삭제
    public void delete(Integer rno);
}