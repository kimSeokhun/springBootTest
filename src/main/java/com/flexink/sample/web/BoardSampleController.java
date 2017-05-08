package com.flexink.sample.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flexink.domain.sample.Board;
import com.flexink.sample.service.BoardSampleService;
import com.flexink.vo.ParamsVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sample")
public class BoardSampleController {

	@Autowired
	BoardSampleService boardSampleService;
	
	
	@GetMapping("/board")
	public String view(ParamsVo params, ModelMap model) {
		model.put("list", boardSampleService.getList(params));
		model.put("type", params.getString("type", ""));
		return "/sample/boardList";
	}
	
	@GetMapping("/viewArticle")
	public String viewArticle(Board board, ParamsVo params, ModelMap model) {
		
		model.put("article", boardSampleService.getArticle(board));
		
		return "/sample/viewArticle";
	}
	
	@GetMapping("/article")
	public String writeAriticle(Board board, ParamsVo params, ModelMap model) {
		if(board.getId() != null) {
			model.put("article", boardSampleService.getArticle(board));
		}
		return "/sample/boardDetail";
	}
	
	@Transactional
	@PostMapping("/article")
	public String saveAriticle(ParamsVo params, @Valid Board board, BindingResult bindingResult) {
		if(params.getString("secret", "").equalsIgnoreCase("true")) {
			board.setSecret(Board.Secret.Y);
		} else {
			board.setSecret(Board.Secret.N);
		}
		
		// 유효성 검증
        if (bindingResult.hasErrors()) {
        	log.debug("Validator Errors : {} ", bindingResult.getAllErrors());
            return "/sample/boardDetail";
        }
        
        boardSampleService.saveArticle(board);
		
		return "redirect:/sample/board?type="+board.getType();
	}
	
	@Transactional
	@PostMapping("/article/update")
	public String updateAriticle(ParamsVo params, @Valid Board board, BindingResult bindingResult) {
		if(params.getString("secret", "").equalsIgnoreCase("true")) {
			board.setSecret(Board.Secret.Y);
		} else {
			board.setSecret(Board.Secret.N);
		}
		
		// 유효성 검증
        if (bindingResult.hasErrors()) {
        	log.debug("Validator Errors : {} ", bindingResult.getAllErrors());
            return "/sample/boardDetail";
        }
        
        boardSampleService.saveArticle(board);
		
		//return "redirect:/sample/board?type="+board.getType();
        return "redirect:/sample/viewArticle?id="+board.getId();
	}
	
	@Transactional
	@PostMapping("/article/delete")
	public String deleteAriticle(@Valid Board board) {
		        
        boardSampleService.saveArticle(board);
		
		return "redirect:/sample/board?type="+board.getType();
	}
}