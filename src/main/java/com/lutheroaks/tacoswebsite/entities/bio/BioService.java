package com.lutheroaks.tacoswebsite.entities.bio;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lutheroaks.tacoswebsite.entities.member.Member;
import com.lutheroaks.tacoswebsite.utils.AuthenticatedDetails;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BioService {
    
    // for logging information to console
	private Logger logger = org.slf4j.LoggerFactory.getLogger(BioService.class);

    @Autowired
    private BioRepo repository;


    @Autowired
    private AuthenticatedDetails authenticatedDetails;

    /**
     * Adds a Bio object to the database
     * @param request
     * @param response
     * @throws IOException
     */
    public Bio createBio(final HttpServletRequest request) throws IOException{
        // get the parameters from the request
        String major = request.getParameter("major");
        String hometown = request.getParameter("hometown");
        String backgroundInfo = request.getParameter("background");
        // get the member based on their login credentials
        Member member = authenticatedDetails.getLoggedInMember(request);

        Bio toAdd = new Bio();
        toAdd.setMember(member);
        toAdd.setMajor(major);
        toAdd.setHometown(hometown);
        toAdd.setBackgroundInfo(backgroundInfo);
        toAdd.setProfilePicture(null);
        // save the new Bio
        repository.save(toAdd);
        return toAdd;
    }

    /**
     * Gets a member's bio from the database
     * @param request
     * @return
     */
    public Bio retrieveBio(final HttpServletRequest request) {
        try{
            // get the member based on their login credentials
            Member member = authenticatedDetails.getLoggedInMember(request);
            Bio toReturn = repository.findBioByMember(member);
            // if this member does not yet have a bio, create one
            if(toReturn == null){
                toReturn = createBio(request);
            }
            return toReturn;
        }catch(Exception e){
            logger.error("an exception occurred while retrieving a bio: ", e);
            return null;
        }
    }
    
    /**
     * Delete's the logged in member's bio
     * @param request
     * @param response
     * @throws IOException
     */
    public void removeBio(final HttpServletRequest request, 
        final HttpServletResponse response) throws IOException {
        // get the member based on their login credentials
        Member member = authenticatedDetails.getLoggedInMember(request);
        // get the logged in member's bio and delete it
        Bio toDelete = repository.findBioByMember(member);
        repository.deleteBioById(toDelete.getBioId());
        response.sendRedirect("index");
    }

    /**
     * Update a member's bio
     * @param request
     * @param response
     * @throws IOException
     */
    public void updateBio(final HttpServletRequest request, final HttpServletResponse response){
        Member tempMember = authenticatedDetails.getLoggedInMember(request);
        Bio tempBio = repository.findBioByMember(tempMember);
        tempBio.setBackgroundInfo(request.getParameter("backgroundInfo"));
        tempBio.setHometown(request.getParameter("hometown"));
        tempBio.setMajor(request.getParameter("major"));
        repository.save(tempBio);
    }

    /**
     * Saves a user's profile picture
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    public void addProfilePicture(final MultipartFile file, 
            final HttpServletRequest request) throws IOException, ServletException {
            
        // get the bio object for the member that submitted the request and save the new photo
        Bio currentBio = retrieveBio(request);

        byte[] photo = file.getBytes();
        currentBio.setProfilePicture(photo);
        repository.save(currentBio);

    }

    /**
     * returns a user's profile picture via servlet response
     * @param id
     * @param response
     * @throws IOException
     */
    public void retrieveProfilePicture(final String id, 
                final HttpServletResponse response) throws IOException{
        
        int bioId = Integer.parseInt(id);
        byte[] image = repository.getProfilePicture(bioId);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(image);
        response.getOutputStream().close(); 
    }
    
}
