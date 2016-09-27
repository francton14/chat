package com.piczon.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by franc on 9/27/2016.
 */
public class UpdatableUser extends User {

   private  String updatableUsername;

    public UpdatableUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.updatableUsername = username;
    }

    public UpdatableUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.updatableUsername = username;
    }

    @Override
    public String getUsername() {
        return updatableUsername;
    }

    public void setUsername(String username) {
        this.updatableUsername = username;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && this.updatableUsername.equals(((User) o).getUsername());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (updatableUsername != null ? updatableUsername.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.updatableUsername).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(isEnabled()).append("; ");
        sb.append("AccountNonExpired: ").append(isAccountNonExpired()).append("; ");
        sb.append("credentialsNonExpired: ").append(isCredentialsNonExpired()).append("; ");
        sb.append("AccountNonLocked: ").append(isAccountNonLocked()).append("; ");
        if(!getAuthorities().isEmpty()) {
            sb.append("Granted Authorities: ");
            boolean first = true;
            Iterator var3 = getAuthorities().iterator();
            while(var3.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority)var3.next();
                if(!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }
        return sb.toString();
    }

}
