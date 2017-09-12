package com.qfree.qfree_facilitator.service;

import com.qfree.qfree_facilitator.model.Facility;

/**
 * Created by Fahad Qureshi on 9/12/2017.
 */

public class FacilityService {
    private static final FacilityService ourInstance = new FacilityService();
    private static final Facility facilityInstance = new Facility();

    public static FacilityService getInstance() {
        return ourInstance;
    }

    private FacilityService() {
    }

    public Facility getFacilityInstance () {
        return facilityInstance;
    }
    public void setFacilityInstance (Facility facility) {
        facilityInstance.setId(facility.getId());
        facilityInstance.setName(facility.getName());
    }

}
