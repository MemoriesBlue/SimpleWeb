package com.elaine.web.converter;

import com.elaine.web.model.Request;
import com.google.common.collect.Maps;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-9-4.
 */
public class MultipartFormConverter extends HttpInputConverter {
    /**
     * servlet上传文件解析器
     */
    private ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());

    public Request convert(HttpServletRequest request) {
        Map<String, Object> paraMaps = Maps.newHashMap();
        try {
            List<FileItem> fileItems = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                paraMaps.put(fileItem.getFieldName(), fileItem);
            }
        } catch (FileUploadException e) {
            throw new RuntimeException("解析文件上传失败", e);
        }
        Request req = new Request();
        req.setParaMaps(paraMaps);
        req.setContentType(parseContentType(request));
        req.setEncoding(parseEncoding(request));
        req.setRequestPattern(buildRequestPattern(request));
        return req;
    }
}
