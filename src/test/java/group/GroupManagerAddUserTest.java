package test.java.group;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import fr.easypass.manager.GroupManager;
import fr.easypass.manager.UserManager;
import fr.easypass.model.User;
import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class GroupManagerAddUserTest extends TestCase {
    
    private static GroupManager groupManager;
    private static UserManager userManager;
    
    public static final Logger log = Logger.getLogger(GroupManagerAddUserTest.class.getName());
    
    @Parameters(name = "Insert group {0}")
    public static List<Object[]> data() {
        
        return Arrays.asList(
            new Object[][] {
                {1, 3},
            }
        );
    }
    
    @Parameter(0)
    public Integer groupId;

    @Parameter(1)
    public Integer userId;
    
    @BeforeClass
    public static void setUpBeforeClass() throws InstantiationException {
        groupManager = new GroupManager();
        userManager = new UserManager();
    }
    
    @Test
    public void testInsertUserGroup(){
        
        Map<String, Map <Integer, User>> result;
        Map<Integer, User> usersDb;
        Integer success;
        
        try {
            
            log.log(Level.INFO, "We're testing add user in group...");
            
            result = userManager.getUsersByGroup(groupId);
            usersDb = result.get("groupUsers");
            assertFalse(usersDb.containsKey(userId));
            
            success = groupManager.addUser(groupId, userId);
            assertEquals(1, success.intValue());
            
            result = userManager.getUsersByGroup(groupId);
            usersDb = result.get("groupUsers");
            assertTrue(usersDb.containsKey(userId));
            
            
        } catch (IOException e1) {
            log.log(Level.SEVERE, "An occured while testing user group adding.", e1);
        }
        
    }
    

}
