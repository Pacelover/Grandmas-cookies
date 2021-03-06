package com.lutheroaks.tacoswebsite.entities.member;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lutheroaks.tacoswebsite.utils.AuthenticatedDetails;

@Service
public class MemberService {

    // for logging information to console
	private Logger logger = org.slf4j.LoggerFactory.getLogger(MemberService.class);
    
    @Autowired
    private MemberRepo repository;

	@Autowired
    private AuthenticatedDetails authenticatedDetails;

	@Autowired 
	private PasswordEncoder passwordEncoder;

    /**
     * Creates and saves a new Tacos Member
     * @param request
     * @param response
     * @throws IOException
     */
    public void createMember(final HttpServletRequest request, 
    final HttpServletResponse response) throws IOException {
        String fName = request.getParameter("fname");
		String lName = request.getParameter("lname");
		String email = request.getParameter("email");
		// if no member is found in the table by the given email, we can create the new member
		if (repository.findMemberByEmail(email) == null) {
			Member toAdd = new Member();
			toAdd.setFirstName(fName);
			toAdd.setLastName(lName);
			toAdd.setEmail(email);
			// this is the encrypted form of the password: "tacos"
			toAdd.setPassword("$2a$10$ga75bkq0QgV63EvFY1iX6.6L0Y7wxdi2yOLAlqklRcmZutMu2ohJy");
			toAdd.setRole("ROLE_ADMIN");
			toAdd.setEnabled(true);
			repository.save(toAdd);
			response.sendRedirect("member-table");
		} else{
			if(logger.isInfoEnabled() && email != null){
				logger.info(String.format("An existing user with the email address: " + 
									"%s was found in the database.", email));
			}
			response.sendRedirect("error");
		}
    }

	/**
	 * Updates member name and email
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updateMemberDetails(final HttpServletRequest request, 
    final HttpServletResponse response) throws IOException {
		Member memberToUpdate = authenticatedDetails.getLoggedInMember(request);
		int memberId = memberToUpdate.getMemberId();
		String fName = request.getParameter("fname");
		String lName = request.getParameter("lname");
		String email = request.getParameter("email");
		// Member must exist in order to be updated
		if (repository.findMemberById(memberId) != null) {
			memberToUpdate.setFirstName(fName);
			memberToUpdate.setLastName(lName);
			memberToUpdate.setEmail(email);
			repository.save(memberToUpdate);
			response.sendRedirect("member-table");
		} else{
			if(logger.isInfoEnabled() && email != null){
				logger.info(String.format("An existing user with the email address: " + 
									"%s was found in the database.", email));
			}
			response.sendRedirect("error");
		}
	}

	/**
	 * Updates member password
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updatePassword(final HttpServletRequest request, 
    final HttpServletResponse response) throws IOException {
		Member memberToUpdate = authenticatedDetails.getLoggedInMember(request);
		int memberId = memberToUpdate.getMemberId();
		String password = request.getParameter("password");
		String encryptedPwd = passwordEncoder.encode(password);
		if (repository.findMemberById(memberId) != null) {
			memberToUpdate.setPassword(encryptedPwd);
			repository.save(memberToUpdate);
			response.sendRedirect("member-table");
		} else{
			response.sendRedirect("error");
		}
	}

    /**
     * Deletes a specified member
     * @param request
     */
    public void removeMember(final HttpServletRequest request) {
        int memberId = Integer.parseInt(request.getParameter("memberId"));
		repository.deleteMemberById(memberId);
    }

}
