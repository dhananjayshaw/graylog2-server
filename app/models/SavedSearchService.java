/**
 * Copyright 2013 Lennart Koopmann <lennart@torch.sh>
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package models;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import lib.APIException;
import lib.ApiClient;
import models.api.requests.searches.CreateSavedSearchRequest;
import models.api.responses.searches.SavedSearchSummaryResponse;
import models.api.responses.searches.SavedSearchesResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
public class SavedSearchService {

    private final ApiClient api;
    private final SavedSearch.Factory savedSearchFactory;

    @Inject
    private SavedSearchService(ApiClient api, SavedSearch.Factory savedSearchFactory) {
        this.api = api;
        this.savedSearchFactory = savedSearchFactory;
    }

    public void create(CreateSavedSearchRequest cssr) throws APIException, IOException {
        api.post().path("/search/saved")
                .body(cssr)
                .expect(201)
                .execute();
    }

    public List<SavedSearch> all() throws APIException, IOException {
        List<SavedSearch> list = Lists.newArrayList();

        SavedSearchesResponse response = api.get(SavedSearchesResponse.class).path("/search/saved").execute();

        for (SavedSearchSummaryResponse search : response.searches) {
            list.add(savedSearchFactory.fromSummaryResponse(search));
        }

        return list;
    }

}
