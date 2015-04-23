package com.common.file;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

/**
 * 
 * 
 * <p>
 * Title：文件上传处理类
 * </p>
 * <p>
 * Description：添加到spring的bean配置中，用于处理文件上传的request
 * </p>
 * <p>
 * Author :李瀛 2012-8-20
 * </p>
 * <p>
 * Department : 平台
 * </p>
 */
public class LyMultipartResolver extends CommonsMultipartResolver implements
		MultipartResolver {

	private boolean resolveLazily = false;

	public void setResolveLazily(boolean resolveLazily) {
		this.resolveLazily = resolveLazily;
	}
	
	public MultipartHttpServletRequest resolveMultipart(
			final HttpServletRequest request) throws MultipartException {
//		// 设置上传的文件最大大小为50M
//		this.getFileUpload().setSizeMax(50 * 1024 * 1024);
		Assert.notNull(request, "Request must not be null");
		DefaultMultipartHttpServletRequest req = null;
		if (this.resolveLazily) {
			req = new DefaultMultipartHttpServletRequest(request) {
				@Override
				protected void initializeMultipart() {
					MultipartParsingResult parsingResult = parseRequest(request);
					setMultipartFiles(parsingResult.getMultipartFiles());
					setMultipartParameters(parsingResult
							.getMultipartParameters());
				}
			};
		} else {
			MultipartParsingResult parsingResult = parseRequest(request);
			req = new DefaultMultipartHttpServletRequest(request,
					parsingResult.getMultipartFiles(),
					parsingResult.getMultipartParameters());
		}
		return req;
	}
}
