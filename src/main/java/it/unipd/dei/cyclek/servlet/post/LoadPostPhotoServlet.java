/*
 * Copyright 2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.cyclek.servlet.post;

import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Post;
import it.unipd.dei.cyclek.dao.post.LoadPostPhotoDAO;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public final class LoadPostPhotoServlet extends AbstractDatabaseServlet {


	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		LogContext.setIPAddress(req.getRemoteAddr());
		LogContext.setAction(Actions.LOAD_POST_PHOTO);

		// request parameter
		int postId = -1;

		// model
		Post e = null;

		try {

			// retrieves the request parameter
			postId = Integer.parseInt(req.getParameter("postId"));

			LogContext.setResource(req.getParameter("postId"));

			// creates a new object for accessing the database and loading the photo of an employees
			e = new LoadPostPhotoDAO(getConnection(), postId).access().getOutputParam();

			if (e.hasPhoto()) {
				res.setContentType(e.getPhotoMediaType());
				res.getOutputStream().write(e.getPhoto());
				res.getOutputStream().flush();

				LOGGER.info("Photo for post %d successfully sent.", postId);
			} else {
				LOGGER.info("Post %d has no  photo and/or valide MIME media type specified.", postId);

				res.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}

		} catch (Exception ex) {
			LOGGER.error("Unable to load the photo of the post.", ex);

			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			LogContext.removeIPAddress();
			LogContext.removeAction();
			LogContext.removeUser();
		}

	}

}
