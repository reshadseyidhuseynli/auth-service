INSERT INTO permissions(id, resource, action) VALUES(1, 'super-admin', 'read');
INSERT INTO permissions(id,resource, action) VALUES(2, 'super-admin', 'insert');
INSERT INTO permissions(id, resource, action) VALUES(3, 'super-admin', 'update');
INSERT INTO permissions(id, resource, action) VALUES(4, 'super-admin', 'delete');
INSERT INTO permissions(id,resource, action) VALUES(5, 'admin', 'read');
INSERT INTO permissions(id, resource, action) VALUES(6, 'admin', 'insert');
INSERT INTO permissions(id, resource, action) VALUES(7, 'admin', 'update');
INSERT INTO permissions(id, resource, action) VALUES(8, 'admin', 'delete');

INSERT INTO roles(id, name) VALUES(1, 'SUPER_ADMIN');
INSERT INTO roles(id, name) VALUES(2, 'ADMIN');

INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 1);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 2);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 3);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 4);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 5);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 6);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 7);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 8);

INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 5);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 6);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 7);
INSERT INTO roles_permissions(role_id, permission_id) VALUES(1, 8);