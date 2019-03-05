package com.home.core.web.endpoint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.home.common.utils.FastDFSUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.home.common.entity.PageQuery;
import com.home.common.entity.ResponseResult;
import com.home.common.utils.DownloadUtils;
import com.home.core.entity.Annex;
import com.home.core.service.AnnexService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "文件接口")
@Controller
@RequestMapping(value = "/api/annex")
public class AnnexEndpoint {

	@Value("${upload.folder}")
	private String root;
	@Autowired
	private AnnexService annexService; 

	@ApiOperation(value = "文件分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public @ResponseBody ResponseResult<Page<Annex>> page(PageQuery pageQuery) {
		Page<Annex> page = annexService.page(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "文件上传", httpMethod = "POST", produces = "application/json")
	@PostMapping("upload")
	public @ResponseBody ResponseResult<Annex> upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		Annex annex = new Annex();
		try {
			annex = annexService.upload(file.getBytes(), file.getOriginalFilename(), null, Annex.TEMP_PICTURE_TYPE, Annex.TEMP_PICTURE_PATH);
 		} catch (IOException e) {
			return ResponseResult.createError(HttpStatus.BAD_REQUEST, "文件上传失败");
		}
		return ResponseResult.createSuccess(annex);
	}

	@ApiOperation(value = "文件删除", httpMethod = "POST", produces = "application/json")
	@PostMapping("remove")
	public @ResponseBody ResponseResult<?> remove(String id) {
		annexService.remove(id);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "附件下载", httpMethod = "GET")
	@RequestMapping(value = "/download/{path1}/{name:.+}", method = RequestMethod.GET)
	public void download(@PathVariable String path1, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = "/" + path1 + "/" + name;
		handle(path, request, response);
	}

	@ApiOperation(value = "附件下载", httpMethod = "GET")
	@RequestMapping(value = "/download/{path1}/{path2}/{name:.+}", method = RequestMethod.GET)
	public void download(@PathVariable String path1, @PathVariable String path2, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = "/" + path1 + "/" + path2 + "/" + name;
		handle(path, request, response);
	}

	@ApiOperation(value = "附件下载", httpMethod = "GET")
	@RequestMapping(value = "/download/{path1}/{path2}/{path3}/{name:.+}", method = RequestMethod.GET)
	public void download(@PathVariable String path1, @PathVariable String path2, @PathVariable String path3, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = "/" + path1 + "/" + path2 + "/" + path3 + "/" + name;
		handle(path, request, response);
	}

	private void handle(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = new File(root + path);
		Annex annex = annexService.getByPath(path);
		String fileName = annex.getName();
		if (StringUtils.isNotBlank(annex.getFastdfsUrl())) {
			try { 
				InputStream input = FastDFSUtils.downloadFileByUrl(annex.getFastdfsUrl());
				DownloadUtils.downloadByInputStream(input, fileName, request, response);
			} catch (Exception e) {
				DownloadUtils.downloadByFile(file, fileName, request, response);
				throw new Exception("FastDFS 文件下载失败");
			}
		} else {
			DownloadUtils.downloadByFile(file, fileName, request, response);
		}
	}
}
