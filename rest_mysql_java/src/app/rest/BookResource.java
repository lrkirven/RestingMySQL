package app.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

import app.dao.BookDAOImpl;
import app.vo.Book;
import app.vo.BookExample;

@Path("/books")
@Component
@Scope("singleton")
public class BookResource {
	
	@Autowired
	private BookDAOImpl bookDAO = null;
	
	@Context 
	UriInfo uriInfo = null;
    
	@Context 
    Request request = null;
	
	String container = null;
	
	// Logger
	private Logger logger = Logger.getLogger(BookResource.class.getName());
	
	@GET @Path("{bookid}")
	@Produces("application/xml")
	public String getBook(@PathParam("bookid") String bookid, @DefaultValue("0") @QueryParam("deleteFlag") int deleteFlag) {
		int i = 0;
		List bookList = null;
		String xmlStr = null;
		XStream xstream = null;
		Book book = null;
		Integer id = null;
		
		try { 
			id = Integer.parseInt(bookid);
		}
		catch (Exception e) {
		}
		logger.info("getBooks(): bookid=" + id);
		
		BookExample where = null;
		if (bookDAO != null) {
			xmlStr = "<Books>";
			if (id != null) {
				where = new BookExample();
				BookExample.Criteria cr = where.createCriteria();
				cr.andBookidEqualTo(id);
				where.or(cr);
			}
			try {
				bookList = bookDAO.selectBookByExample(where);
				if (bookList != null && bookList.size() > 0) {
					xstream = new XStream();
					xstream.alias("Book", Book.class);
					for (i=0; i<bookList.size(); i++) {
						book = (Book)bookList.get(i);
						xmlStr += xstream.toXML(book);
					}
				}
				if (deleteFlag == 1 && book != null) {
					int rows = bookDAO.deleteBookByPrimaryKey(book.getBookid());
				}
			}
			catch (Exception e) {
				logger.error("getBooks: " + getStackTrace(e));
			}
			
			xmlStr += "</Books>";
		}
		return xmlStr;
	}
	
	@GET 
	@Produces("application/xml")
	public String getBooks() {
		int i = 0;
		List bookList = null;
		String xmlStr = null;
		XStream xstream = null;
		Book book = null;
		Integer id = null;
		
	
		logger.info("getBooks(): ALL");
		
		if (bookDAO != null) {
			xmlStr = "<Books>";
			try {
				bookList = bookDAO.selectBookByExample(null);
				if (bookList != null && bookList.size() > 0) {
					xstream = new XStream();
					xstream.alias("Book", Book.class);
					for (i=0; i<bookList.size(); i++) {
						book = (Book)bookList.get(i);
						xmlStr += xstream.toXML(book);
					}
				}
			}
			catch (Exception e) {
				logger.error("getBooks: " + getStackTrace(e));
			}
			
			xmlStr += "</Books>";
		}
		return xmlStr;
	}
	
	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	public String addOrUpdateBook(String bookContent) {
		XStream xstream = null;
		String res = null;
		Integer newBookId = null;
		Integer bookId = null;
		int rows = 0;
		
		if (bookContent != null) {
			xstream = new XStream();
			Book book = (Book)xstream.fromXML(bookContent);
			// made id null to get auto generated id
			bookId = book.getBookid();
			try {
				//
				// adding a new book
				//
				if (bookId == -1) {
					book.setBookid(null);
					newBookId = bookDAO.insertBookSelective(book);
					book.setBookid(newBookId);
					xstream.alias("Book", Book.class);
					// serialize new book object
					res = xstream.toXML(book);
				}
				else {
					rows = bookDAO.updateBookByPrimaryKeySelective(book);
					if (rows > 0) {
						xstream.alias("Book", Book.class);
						// serialize new book object
						res = xstream.toXML(book);
						
					}
				}
			}
			catch (Exception e) {
				logger.error("addBook: " + getStackTrace(e));
			}
		}
		return ("<Books>" + res + "</Books>");
	}
	

	private String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String str = "\n" + sw.toString();
        return str;
	}

}
