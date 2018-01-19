package com.znyw.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private final byte[] body;

	public byte[] getBody() {
		return body;
	}

	@SuppressWarnings("rawtypes")
	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		Enumeration e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = request.getHeader(name);
			System.out.println(name + " = " + value);

		}
		body = HttpHelper.getBodyString(request).getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

	@Override
	public String getHeader(String name) {
		return super.getHeader(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getHeaderNames() {
		return super.getHeaderNames();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getHeaders(String name) {
		return super.getHeaders(name);
	}
}
