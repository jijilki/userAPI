package com.neo.userAPI.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Predicate;

public class TodoUtil {


    public static boolean hasWriteAccess() {

        Predicate<GrantedAuthority> writeAccess = p -> p.getAuthority().contains("ADMIN_ACCESS") || p.getAuthority().contains("ADMIN_ACCESS");

        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream().anyMatch(writeAccess);

    }

    public static boolean hasReadAccess() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream().anyMatch(p -> p.getAuthority().contains("READ_ACCESS"));
    }

  /*  private static List<String> getUserAccessList() {
        List<String> userRoles = SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getAuthorities()
                                                    .stream()
                                                    .map(x -> x.getAuthority())
                                                    .collect(Collectors.toList());
        return userRoles;
    }*/


}
