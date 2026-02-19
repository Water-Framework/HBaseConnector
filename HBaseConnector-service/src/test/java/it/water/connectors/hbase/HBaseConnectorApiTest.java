package it.water.connectors.hbase;

import it.water.connectors.hbase.api.HBaseConnectorApi;
import it.water.connectors.hbase.api.HBaseConnectorRepository;
import it.water.connectors.hbase.api.HBaseConnectorSystemApi;
import it.water.connectors.hbase.model.HBaseConnector;
import it.water.core.api.bundle.Runtime;
import it.water.core.api.model.PaginableResult;
import it.water.core.api.model.Role;
import it.water.core.api.permission.PermissionManager;
import it.water.core.api.registry.ComponentRegistry;
import it.water.core.api.repository.query.Query;
import it.water.core.api.role.RoleManager;
import it.water.core.api.service.Service;
import it.water.core.api.user.UserManager;
import it.water.core.interceptors.annotations.Inject;
import it.water.core.model.exceptions.ValidationException;
import it.water.core.model.exceptions.WaterRuntimeException;
import it.water.core.permission.exceptions.UnauthorizedException;
import it.water.core.testing.utils.bundle.TestRuntimeInitializer;
import it.water.core.testing.utils.junit.WaterTestExtension;
import it.water.core.testing.utils.runtime.TestRuntimeUtils;
import it.water.repository.entity.model.exceptions.DuplicateEntityException;
import it.water.repository.entity.model.exceptions.NoResultException;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Generated with Water Generator.
 * Test class for HBaseConnector services.
 */
@ExtendWith(WaterTestExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HBaseConnectorApiTest implements Service {

    @Inject
    @Setter
    private ComponentRegistry componentRegistry;

    @Inject
    @Setter
    private HBaseConnectorApi hbaseconnectorApi;

    @Inject
    @Setter
    private Runtime runtime;

    @Inject
    @Setter
    private HBaseConnectorRepository hbaseconnectorRepository;

    @Inject
    @Setter
    private PermissionManager permissionManager;

    @Inject
    @Setter
    private UserManager userManager;

    @Inject
    @Setter
    private RoleManager roleManager;

    private it.water.core.api.model.User adminUser;
    private it.water.core.api.model.User hbaseconnectorManagerUser;
    private it.water.core.api.model.User hbaseconnectorViewerUser;
    private it.water.core.api.model.User hbaseconnectorEditorUser;

    private Role hbaseconnectorManagerRole;
    private Role hbaseconnectorViewerRole;
    private Role hbaseconnectorEditorRole;

    @BeforeAll
    void beforeAll() {
        hbaseconnectorManagerRole = roleManager.getRole(HBaseConnector.DEFAULT_MANAGER_ROLE);
        hbaseconnectorViewerRole = roleManager.getRole(HBaseConnector.DEFAULT_VIEWER_ROLE);
        hbaseconnectorEditorRole = roleManager.getRole(HBaseConnector.DEFAULT_EDITOR_ROLE);
        Assertions.assertNotNull(hbaseconnectorManagerRole);
        Assertions.assertNotNull(hbaseconnectorViewerRole);
        Assertions.assertNotNull(hbaseconnectorEditorRole);

        adminUser = userManager.findUser("admin");
        hbaseconnectorManagerUser = userManager.addUser("manager", "name", "lastname", "manager@a.com", "TempPassword1_", "salt", false);
        hbaseconnectorViewerUser = userManager.addUser("viewer", "name", "lastname", "viewer@a.com", "TempPassword1_", "salt", false);
        hbaseconnectorEditorUser = userManager.addUser("editor", "name", "lastname", "editor@a.com", "TempPassword1_", "salt", false);

        roleManager.addRole(hbaseconnectorManagerUser.getId(), hbaseconnectorManagerRole);
        roleManager.addRole(hbaseconnectorViewerUser.getId(), hbaseconnectorViewerRole);
        roleManager.addRole(hbaseconnectorEditorUser.getId(), hbaseconnectorEditorRole);

        TestRuntimeUtils.impersonateAdmin(componentRegistry);
    }

    @Test
    @Order(1)
    void componentsInsantiatedCorrectly() {
        this.hbaseconnectorApi = this.componentRegistry.findComponent(HBaseConnectorApi.class, null);
        Assertions.assertNotNull(this.hbaseconnectorApi);
        Assertions.assertNotNull(this.componentRegistry.findComponent(HBaseConnectorSystemApi.class, null));
        this.hbaseconnectorRepository = this.componentRegistry.findComponent(HBaseConnectorRepository.class, null);
        Assertions.assertNotNull(this.hbaseconnectorRepository);
    }

    @Test
    @Order(2)
    void saveOk() {
        HBaseConnector entity = createHBaseConnector(0);
        entity = this.hbaseconnectorApi.save(entity);
        Assertions.assertEquals(1, entity.getEntityVersion());
        Assertions.assertTrue(entity.getId() > 0);
        Assertions.assertEquals("hbaseConnector0", entity.getName());
    }

    @Test
    @Order(3)
    void updateShouldWork() {
        Query q = this.hbaseconnectorRepository.getQueryBuilderInstance().createQueryFilter("name=hbaseConnector0");
        HBaseConnector entity = this.hbaseconnectorApi.find(q);
        Assertions.assertNotNull(entity);
        entity.setName("hbaseConnectorUpdated");
        entity = this.hbaseconnectorApi.update(entity);
        Assertions.assertEquals("hbaseConnectorUpdated", entity.getName());
        Assertions.assertEquals(2, entity.getEntityVersion());
    }

    @Test
    @Order(4)
    void updateShouldFailWithWrongVersion() {
        Query q = this.hbaseconnectorRepository.getQueryBuilderInstance().createQueryFilter("name=hbaseConnectorUpdated");
        HBaseConnector errorEntity = this.hbaseconnectorApi.find(q);
        Assertions.assertEquals("hbaseConnectorUpdated", errorEntity.getName());
        Assertions.assertEquals(2, errorEntity.getEntityVersion());
        errorEntity.setEntityVersion(1);
        Assertions.assertThrows(WaterRuntimeException.class, () -> this.hbaseconnectorApi.update(errorEntity));
    }

    @Test
    @Order(5)
    void findAllShouldWork() {
        PaginableResult<HBaseConnector> all = this.hbaseconnectorApi.findAll(null, -1, -1, null);
        Assertions.assertTrue(all.getResults().size() >= 1);
    }

    @Test
    @Order(6)
    void findAllPaginatedShouldWork() {
        for (int i = 2; i < 11; i++) {
            HBaseConnector u = createHBaseConnector(i);
            this.hbaseconnectorApi.save(u);
        }
        PaginableResult<HBaseConnector> paginated = this.hbaseconnectorApi.findAll(null, 7, 1, null);
        Assertions.assertEquals(7, paginated.getResults().size());
        Assertions.assertEquals(1, paginated.getCurrentPage());
        Assertions.assertEquals(2, paginated.getNextPage());
        paginated = this.hbaseconnectorApi.findAll(null, 7, 2, null);
        Assertions.assertTrue(paginated.getResults().size() >= 3);
        Assertions.assertEquals(2, paginated.getCurrentPage());
        Assertions.assertEquals(1, paginated.getNextPage());
    }

    @Test
    @Order(7)
    void removeAllShouldWork() {
        PaginableResult<HBaseConnector> paginated = this.hbaseconnectorApi.findAll(null, -1, -1, null);
        paginated.getResults().forEach(entity -> this.hbaseconnectorApi.remove(entity.getId()));
        Assertions.assertEquals(0, this.hbaseconnectorApi.countAll(null));
    }

    @Test
    @Order(8)
    void saveShouldFailOnDuplicatedEntity() {
        HBaseConnector entity = createHBaseConnector(1);
        this.hbaseconnectorApi.save(entity);
        HBaseConnector duplicated = this.createHBaseConnector(1);
        Assertions.assertThrows(DuplicateEntityException.class, () -> this.hbaseconnectorApi.save(duplicated));

        HBaseConnector secondEntity = createHBaseConnector(2);
        this.hbaseconnectorApi.save(secondEntity);
        entity.setName("hbaseConnector2");
        Assertions.assertThrows(DuplicateEntityException.class, () -> this.hbaseconnectorApi.update(entity));
    }

    @Test
    @Order(9)
    void saveShouldFailOnValidationFailure() {
        HBaseConnector newEntity = new HBaseConnector(
            "<script>function(){alert('ciao')!}</script>",
            "localhost",
            16000,
            "localhost",
            "hdfs://localhost:8020/hbase",
            1L
        );
        Assertions.assertThrows(ValidationException.class, () -> this.hbaseconnectorApi.save(newEntity));
    }

    @Order(10)
    @Test
    void managerCanDoEverything() {
        TestRuntimeInitializer.getInstance().impersonate(hbaseconnectorManagerUser, runtime);
        HBaseConnector entity = createHBaseConnector(101);
        HBaseConnector savedEntity = Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.save(entity));
        savedEntity.setName("managerUpdatedConnector");
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.update(savedEntity));
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.find(savedEntity.getId()));
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.remove(savedEntity.getId()));
    }

    @Order(11)
    @Test
    void viewerCannotSaveOrUpdateOrRemove() {
        TestRuntimeInitializer.getInstance().impersonate(hbaseconnectorViewerUser, runtime);
        HBaseConnector entity = createHBaseConnector(201);
        Assertions.assertThrows(UnauthorizedException.class, () -> this.hbaseconnectorApi.save(entity));
        Assertions.assertEquals(0, this.hbaseconnectorApi.findAll(null, -1, -1, null).getResults().size());
    }

    @Order(12)
    @Test
    void editorCannotRemove() {
        TestRuntimeInitializer.getInstance().impersonate(hbaseconnectorEditorUser, runtime);
        HBaseConnector entity = createHBaseConnector(301);
        HBaseConnector savedEntity = Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.save(entity));
        savedEntity.setName("editorUpdatedConnector");
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.update(savedEntity));
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.find(savedEntity.getId()));
        long savedEntityId = savedEntity.getId();
        Assertions.assertThrows(UnauthorizedException.class, () -> this.hbaseconnectorApi.remove(savedEntityId));
    }

    @Order(13)
    @Test
    void ownedResourceShouldBeAccessedOnlyByOwner() {
        TestRuntimeInitializer.getInstance().impersonate(hbaseconnectorEditorUser, runtime);
        HBaseConnector entity = createHBaseConnector(401);
        HBaseConnector savedEntity = Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.save(entity));
        Assertions.assertDoesNotThrow(() -> this.hbaseconnectorApi.find(savedEntity.getId()));

        TestRuntimeInitializer.getInstance().impersonate(hbaseconnectorManagerUser, runtime);
        long savedEntityId = savedEntity.getId();
        Assertions.assertThrows(NoResultException.class, () -> this.hbaseconnectorApi.find(savedEntityId));
    }

    private HBaseConnector createHBaseConnector(int seed) {
        return new HBaseConnector(
            "hbaseConnector" + seed,
            "localhost",
            16000,
            "localhost",
            "hdfs://localhost:8020/hbase",
            (long) (seed + 1)
        );
    }
}
