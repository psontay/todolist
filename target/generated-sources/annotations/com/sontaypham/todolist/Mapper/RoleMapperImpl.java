package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Entities.Permission;
import com.sontaypham.todolist.Entities.Role;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-26T20:35:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Role toRole(RoleRequest roleRequest) {
        if ( roleRequest == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.name( roleRequest.getName() );
        role.description( roleRequest.getDescription() );

        return role.build();
    }

    @Override
    public RoleResponse toRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.name( role.getName() );
        roleResponse.description( role.getDescription() );
        roleResponse.permissions( permissionSetToPermissionResponseSet( role.getPermissions() ) );

        return roleResponse.build();
    }

    @Override
    public void updateRoleFromRequest(RoleRequest roleRequest, Role role) {
        if ( roleRequest == null ) {
            return;
        }

        role.setName( roleRequest.getName() );
        role.setDescription( roleRequest.getDescription() );
    }

    protected Set<PermissionResponse> permissionSetToPermissionResponseSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionResponse> set1 = new LinkedHashSet<PermissionResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionMapper.toPermissionResponse( permission ) );
        }

        return set1;
    }
}
