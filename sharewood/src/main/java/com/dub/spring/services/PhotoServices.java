package com.dub.spring.services;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.security.core.parameters.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.dub.spring.entities.Photo;


/**
 * Main service interface with methods protected by @PreAuthorize annotation
 * */
public interface PhotoServices {
	
	@PreAuthorize("authentication.name.equals(#username) and " +
					"hasAuthority('USER')")
	InputStream loadPhoto(long id, String username) 
									throws FileNotFoundException;
	
	@PreAuthorize("authentication.name.equals(#username) and " +
			"hasAuthority('USER')")
	List<Photo> getPhotosForCurrentUser(String username);
	
	@PreAuthorize("hasAuthority('USER')")
	List<Photo> getSharedPhotos();
	
	@PreAuthorize("hasAuthority('USER')")
	long createPhoto(
			MultipartFile uploadedFileRef, 
			String username,
			String title,
			boolean shared) throws IOException ;
	
	
	@PreAuthorize("authentication.name.equals(#username) and " +
			"hasAuthority('USER')")
	void deletePhoto(long id, @P("username")String username) throws Exception;
	
		
	@PreAuthorize("authentication.name.equals(#username) and " +
			"hasAuthority('USER')")
	Photo getPhoto(long id, String username);
		
	
	@PreAuthorize("authentication.name.equals(#username) and " +
			"hasAuthority('USER')")
	void updatePhoto(Photo photo, @P("username")String username);
}
