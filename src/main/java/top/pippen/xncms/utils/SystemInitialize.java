package top.pippen.xncms.utils;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.expands.script.engine.DynamicScriptEngine;
import org.hswebframework.expands.script.engine.DynamicScriptEngineFactory;
import org.hswebframework.ezorm.rdb.mapping.SyncRepository;
import org.hswebframework.ezorm.rdb.mapping.defaults.record.Record;
import org.hswebframework.ezorm.rdb.operator.DatabaseOperator;
import org.hswebframework.web.starter.initialize.Dependency;
import org.hswebframework.web.starter.initialize.SimpleDependencyInstaller;
import org.hswebframework.web.starter.initialize.SystemVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhouhao
 */
public class SystemInitialize {
    private final Logger logger = LoggerFactory.getLogger(SystemInitialize.class);

    private final DatabaseOperator database;
    //将要安装的信息
    private final SystemVersion targetVersion;

    //已安装的信息
    private SystemVersion installed;

    private List<SimpleDependencyInstaller> readyToInstall = new ArrayList<>();

    @Setter
    @Getter
    private List<String> excludeTables;

    private String installScriptPath = "classpath*:xncms-user-auth.js";

    private Map<String, Object> scriptContext = new HashMap<>();

    private boolean initialized = false;

    private SyncRepository<Record, String> system;

    public SystemInitialize(DatabaseOperator database, SystemVersion targetVersion) {
        this.database = database;
        this.targetVersion = targetVersion;
    }


    public void init() {
        if (initialized) {
            return;
        }

        scriptContext.put("database", database);
        //scriptContext.put("logger", logger);
        initialized = true;
    }

    public void addScriptContext(String var, Object val) {
        scriptContext.put(var, val);
    }

    protected Map<String, Object> getScriptContext() {
        return new HashMap<>(scriptContext);
    }

    protected void doInstall() {
        List<SimpleDependencyInstaller> doInitializeDep = new ArrayList<>();
        List<Dependency> installedDependencies =
                readyToInstall.stream().map(installer -> {
                    Dependency dependency = installer.getDependency();
                    Dependency installed = getInstalledDependency(dependency.getGroupId(), dependency.getArtifactId());
                    //安装依赖
                    if (installed == null) {
                        doInitializeDep.add(installer);
                          installer.doInstall(getScriptContext());
                    }
                    //更新依赖
                    if (installed == null || installed.compareTo(dependency) < 0) {
                         installer.doUpgrade(getScriptContext(), installed);
                    }
                    return dependency;
                }).collect(Collectors.toList());

        for (SimpleDependencyInstaller installer : doInitializeDep) {
            installer.doInitialize(getScriptContext());
        }
        targetVersion.setDependencies(installedDependencies);
    }

    private Dependency getInstalledDependency(String groupId, String artifactId) {
        if (installed == null) {
            return null;
        }
        return installed.getDependency(groupId, artifactId);
    }

    private SimpleDependencyInstaller getReadyToInstallDependency(String groupId, String artifactId) {
        if (readyToInstall == null) {
            return null;
        }
        return readyToInstall.stream()
                .filter(installer -> installer.getDependency().isSameDependency(groupId, artifactId))
                .findFirst().orElse(null);
    }

    private void initReadyToInstallDependencies() {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("js");
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(installScriptPath);
            List<SimpleDependencyInstaller> installers = new ArrayList<>();
            for (Resource resource : resources) {
                String script = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                SimpleDependencyInstaller installer = new SimpleDependencyInstaller();
                engine.compile("__xncms", script);
                Map<String, Object> context = getScriptContext();
                context.put("dependency", installer);
                engine.execute("__xncms", context).getIfSuccess();
                installers.add(installer);
            }
            readyToInstall = installers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            engine.remove("__xncms");
        }

    }


    public void install() throws Exception {
        init();
        initReadyToInstallDependencies();
        doInstall();
    }
}
