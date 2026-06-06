insert into users.roles (name, description)
values ('DEFAULT', 'Default user'),
       ('PRODUCER', 'Producer user'),
       ('RELIABLE', 'User that can approve/disapprove and edit plays'),
       ('ADMIN', 'Administrator');

INSERT INTO users.permissions (name, description)
VALUES ('plays:view', 'Can view plays'),
       ('plays:create', 'Can add new plays'),
       ('plays:edit:any', 'Can edit any play'),
       ('plays:edit:own', 'Can edit own plays only'),
       ('plays:verify:any', 'Can verify/unverify any play'),
       ('plays:verify:own', 'Can verify/unverify own plays only'),
       ('users:edit:own', 'Can edit own user info');


insert into users.role_permissions (role_id, permission_id)
select r.id, p.id
from users.roles r
         join users.permissions p on p.name in (
                                          'plays:view',
                                          'plays:create',
                                          'users:edit:own'
    )
where r.name = 'DEFAULT';

insert into users.role_permissions (role_id, permission_id)
select r.id, p.id
from users.roles r
         join users.permissions p on p.name in (
                                          'plays:view',
                                          'plays:create',
                                          'users:edit:own',
                                          'plays:edit:any',
                                          'plays:verify:any'
    )
where r.name = 'RELIABLE';

insert into users.role_permissions(role_id, permission_id)
select r.id, p.id
from users.roles r
         join users.permissions p on p.name in (
                                          'plays:view',
                                          'plays:create',
                                          'users:edit:own',
                                          'plays:edit:own',
                                          'plays:verify:own'
    )
where r.name = 'PRODUCER';

insert into users.role_permissions (role_id, permission_id)
select r.id, p.id
from users.roles r
         join users.permissions p on true
where r.name = 'ADMIN';