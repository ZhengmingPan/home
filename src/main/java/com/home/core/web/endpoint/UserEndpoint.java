package com.home.core.web.endpoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.home.common.entity.PageQuery;
import com.home.common.http.ResponseResult;
import com.home.core.entity.Annex;
import com.home.core.entity.BaseUser;
import com.home.core.entity.User;
import com.home.core.service.AnnexService;
import com.home.core.service.BaseUserService;
import com.home.core.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserEndpoint {

	@Autowired
	private UserService userService;
	@Autowired
	private BaseUserService baseUserService;
	@Autowired
	private AnnexService annexService;

	@ApiOperation(value = "头像上传", httpMethod = "POST", produces = "application/json")
	@PostMapping("saveAvatar")
	public ResponseResult<String> saveAvatar(Long id, @RequestParam("avatar") MultipartFile avatar) {
		Annex annex = new Annex();
		try {
			annex = annexService.upload(avatar.getBytes(), avatar.getOriginalFilename(), id == null ? "" : id.toString(), User.PICTURE_TYPE, User.PICTURE_PATH);
			if (id != null) {
				User user = userService.get(id);
				user.setAvatar(annex.getPath());
				userService.save(user);
			}
		} catch (IOException e) {
			return ResponseResult.createError(HttpStatus.BAD_REQUEST, "头像上传失败");
		}
		return ResponseResult.createSuccess(annex.getPath());
	}

	@ApiOperation(value = "用户智能搜索", httpMethod = "GET", produces = "application/json")
	@GetMapping("search")
	public ResponseResult<Page<BaseUser>> search(PageQuery pageQuery) {
		Page<BaseUser> users = baseUserService.pageToSearch(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(users);
	}

	@ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<User>> page(String loginName, String name, String mobile, String status, Date startDate, Date endDate, PageQuery pageQuery) {
		Page<User> users = userService.page(loginName, name, mobile, status, startDate, endDate, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(users);
	}

	@ApiOperation(value = "用户保存", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<User> save(@ModelAttribute("preloadUser") User user, HttpServletRequest request) {
		if (user.getId() == null) {
			user = userService.register(user, request);
		} else {
			user = userService.save(user);
		}
		return ResponseResult.createSuccess(user);
	}

	@ApiOperation(value = "密码重置", httpMethod = "POST", produces = "application/json")
	@PostMapping("entryPassword")
	public ResponseResult<?> entryPassword(Long id, String plainPassword) {
		userService.entryPasword(id, plainPassword);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "用户启用", httpMethod = "POST", produces = "application/json")
	@PostMapping("start")
	public ResponseResult<?> start(Long id) {
		userService.start(id);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "用户停用", httpMethod = "POST", produces = "application/json")
	@PostMapping("stop")
	public ResponseResult<?> stop(Long id) {
		userService.stop(id);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "获取用户信息", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<User> get(Long id) {
		User user = userService.get(id);
		return ResponseResult.createSuccess(user);
	}

	@ApiOperation(value = "验证用户名是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existLoginName")
	public ResponseResult<Boolean> existLoginName(Long id, String loginName) {
		return ResponseResult.createSuccess(userService.existLoginName(id, loginName));
	}

	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id == null) {
			return new User();
		}
		return userService.get(id);
	}

	// 临时接口日期转换 TODO:全局注册
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
