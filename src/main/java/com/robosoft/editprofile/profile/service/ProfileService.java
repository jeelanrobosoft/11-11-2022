package com.robosoft.editprofile.profile.service;


import com.robosoft.editprofile.AdminPanel.servicechk.AdminServicechk;
import com.robosoft.editprofile.profile.dao.ProfileDao;
import com.robosoft.editprofile.profile.model.ChangePassword;
import com.robosoft.editprofile.profile.model.SaveProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class ProfileService {


    @Autowired
    ProfileDao profileDao;

    @Autowired
    AdminServicechk adminServicechk;



    public void saveMyProfile(SaveProfile saveProfile) throws IOException, ParseException {
        String profilePhotoLink = null;
        String finalDateOfBirth = null;
        if (saveProfile.getProfilePhoto() != null) {
            profilePhotoLink = adminServicechk.getFileUrl(saveProfile.getProfilePhoto());
        }
        if(saveProfile.getDateOfBirth() != null){
        Date dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(saveProfile.getDateOfBirth());
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        finalDateOfBirth = newFormat.format(dateOfBirth);
        }
        profileDao.saveProfile(saveProfile, profilePhotoLink, finalDateOfBirth);
    }

    public String changePassword(ChangePassword password) {
        return profileDao.changePassword(password);
    }
}

