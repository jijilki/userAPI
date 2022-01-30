package com.neo.userAPI.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public  class TodoUtil {


    public static boolean hasWriteAccess() {
        return getUserAccessList().contains("WRITE_ACCESS") || getUserAccessList().contains("ADMIN_ACCESS") ;
    }

    public static boolean hasReadAccess() {
        return getUserAccessList().contains("WRITE_ACCESS") || getUserAccessList().contains("ADMIN_ACCESS") || getUserAccessList().contains("WRITE_ACCESS")  ;
    }

    private static List<String> getUserAccessList() {
        List<String> userRoles = SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getAuthorities()
                                                    .stream()
                                                    .map(x -> x.getAuthority()
                                                               .toString())
                                                    .collect(Collectors.toList());
        return userRoles;
    }


}
