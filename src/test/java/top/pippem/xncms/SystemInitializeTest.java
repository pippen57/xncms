package top.pippem.xncms;

import org.hswebframework.ezorm.rdb.operator.DatabaseOperator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication .class)
public class SystemInitializeTest {


    @Autowired
    DatabaseOperator databaseOperator;

    @Test
    public void test(){
        // Assert.assertTrue(databaseOperator.getMetadata().getTable("s_user").isPresent());
        databaseOperator.ddl().createOrAlter("ceshi")
                .addColumn().name("title").varchar(255).comment("标题").commit()
                .commit().sync();
    }

}