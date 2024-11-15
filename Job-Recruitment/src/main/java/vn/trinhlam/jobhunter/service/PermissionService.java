package vn.trinhlam.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Permission;
import vn.trinhlam.jobhunter.domain.response.ResultPaginationDTO;
import vn.trinhlam.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
                permission.getApiPath(), permission.getMethod());
    }

    public Permission create(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission fetchById(long id) {
        Optional<Permission> permissOptional = this.permissionRepository.findById(id);
        if (permissOptional.isPresent())
            return permissOptional.get();
        return null;
    }

    public Permission update(Permission permission) {
        Permission permissionInDB = this.fetchById(permission.getId());

        if (permissionInDB != null) {
            permissionInDB.setApiPath(permission.getApiPath());
            permissionInDB.setName(permission.getName());
            permissionInDB.setMethod(permission.getMethod());
            permissionInDB.setModule(permission.getModule());

            permissionInDB = this.permissionRepository.save(permissionInDB);
            return permissionInDB;
        }
        return null;
    }

    public void delete(long id) {
        Optional<Permission> permissOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pPermissions.getTotalPages());
        mt.setTotal(pPermissions.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pPermissions.getContent());
        return rs;
    }

    public boolean isSameName(Permission permission) {
        Permission permissionDB = this.fetchById(permission.getId());
        if (permissionDB != null) {
            if (permissionDB.getName().equals(permission.getName()))
                return true;
        }

        return false;
    }
}
