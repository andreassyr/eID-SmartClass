package gr.aegean.eIdEuSmartClass;

import gr.aegean.eIdEuSmartClass.model.dao.ClassRoomStateRepository;
import gr.aegean.eIdEuSmartClass.model.dao.GenderRepository;
import gr.aegean.eIdEuSmartClass.model.dao.RoleRepository;
import gr.aegean.eIdEuSmartClass.model.dmo.ClassRoomState;
import gr.aegean.eIdEuSmartClass.model.dao.ActiveCodeRepository;
import gr.aegean.eIdEuSmartClass.model.dao.ClassRoomRepository;
import gr.aegean.eIdEuSmartClass.model.dao.SkypeRoomRepository;
import gr.aegean.eIdEuSmartClass.model.dao.TeamsRepository;
import gr.aegean.eIdEuSmartClass.model.dmo.User;
import gr.aegean.eIdEuSmartClass.model.dao.UserRepository;
import gr.aegean.eIdEuSmartClass.model.dmo.ActiveCode;
import gr.aegean.eIdEuSmartClass.model.dmo.ActiveCodePK;
import gr.aegean.eIdEuSmartClass.model.dmo.ClassRoom;
import gr.aegean.eIdEuSmartClass.model.dmo.Gender;
import gr.aegean.eIdEuSmartClass.model.dmo.Role;
import gr.aegean.eIdEuSmartClass.model.dmo.SkypeRoom;
import gr.aegean.eIdEuSmartClass.model.dmo.Teams;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import java.util.Optional;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbIntegrationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private ClassRoomStateRepository classRoomStateRepository;

    @Autowired
    private ClassRoomRepository classRepo;

    @Autowired
    private ActiveCodeRepository activeRepo;

    @Autowired
    private SkypeRoomRepository skypeRepo;

    @Autowired
    private TeamsRepository teamsRepo;
    
    
    @Test
    @Transactional
    public void createNewUser() {

        Role storedRole = roleRepository.findByName("unregistered").get();
        Gender storedGender = genderRepository.findByName(Gender.MALE).get();

        LocalDate birthday = LocalDate.now();
        User user = new User(storedRole, "eidas-id2", "name2", "surname2", "email", "1234", "ntua", "gr", 
                storedGender, birthday, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),"engName","engSurname");
        //new User("eidas-id2", "name2", "surname", birthday, r, g);

        System.out.println(user.toString());
        userRepository.save(user);

        User user2 = userRepository.findById(user.getId()).get();
        System.out.println(user2.toString());
        assertEquals(user.getCurrentGivenName(), user2.getCurrentGivenName());

    }

    @Test
    public void testFindUserByEidas() {
        Role r = new Role("test");
        Gender g = new Gender("n/a");
        LocalDate birthday = LocalDate.now();
        User user = new User(r, "eidas-id4", "name2", "surname2", "email", "1234", "ntua", "gr", g, birthday, 
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),"engName","engSurname");
        System.out.println(user.toString());
        userRepository.save(user);
        User user3 = userRepository.findFirstByEIDASId("eidas-id4").get();
        assertEquals(user3.getEid(), "eidas-id4");

    }

    @Test
    @Transactional
    public void saveRoomState() {
        ClassRoomState state = new ClassRoomState();
        state.setName("testState");
        classRoomStateRepository.save(state);
    }

//    @Test
//    public void saveState(){
//        RoomState s = new RoomState();
//        s.setName("Restricted");
//        statesRepo.save(s);
//    }
    @Test
    @Transactional
    public void saveClassRomm() {
        ClassRoom room = new ClassRoom();
        room.setName("testName");
//        Optional<RoomState> state2 = statesRepo.findById(new Long(1));
        Optional<ClassRoomState> state2 = classRoomStateRepository.findByName("Restricted");
//        List<RoomState> states = statesRepo.findAll();
        if (state2.isPresent()) {
            room.setState(state2.get());
        }
        classRepo.save(room);
    }

    @Test
    @Transactional
    public void testActiveCode() {

        Role r = roleRepository.findFirstByName(Role.UNREGISTERED).get();
        Gender g = genderRepository.findFirstByName(Gender.UNSPECIFIED).get();
        LocalDate birthday = LocalDate.now();
        User user = new User(r, "eidas-id3", "name2", "surname2", "email", "1234", "ntua", "gr", g, birthday, 
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),"engName","engSurname");

        Role r2 = roleRepository.findFirstByName(Role.UNREGISTERED).get();
        Gender g2 = genderRepository.findFirstByName(Gender.UNSPECIFIED).get();
        User user2 = new User(r2, "eidas-id5", "name5", "surname5", "email2", "12344", "ntua", "gr", g2, 
                birthday, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),"engName","engSurname");
        userRepository.save(user);
        userRepository.save(user2);

        ClassRoom room = new ClassRoom();
        room.setName("testName2");
        Optional<ClassRoomState> state2 = classRoomStateRepository.findByName("Restricted");
        if (state2.isPresent()) {
            room.setState(state2.get());
        }

        classRepo.save(room);

        ActiveCode ac2 = new ActiveCode();
        ac2.setGrantedAt(LocalDateTime.now());
        ac2.setContent("testContent");
        ActiveCodePK key = new ActiveCodePK();
        key.setClassRoom(room);
        key.setUser(user);
        ac2.setId(key);
        activeRepo.save(ac2);

        ActiveCode ac3 = new ActiveCode();
        ac3.setGrantedAt(LocalDateTime.now());
        ac3.setContent("testContent2");
        ActiveCodePK key2 = new ActiveCodePK();
        key2.setClassRoom(room);
        key2.setUser(user2);
        ac3.setId(key2);
        activeRepo.save(ac3);
        assertEquals(activeRepo.getContentFromClassRoom("testName2").contains("testContent"), true);
//        assertEquals(activeRepo.getContentFromClassRoom("testName2").contains("testContent2"), true);

        List<ActiveCode> codes = activeRepo.getCodeFromContent("testContent");
        assertEquals(codes.size(), 1);
        assertEquals(codes.get(0).getContent(), "testContent");
    }

    @Test
    @Transactional
    public void createClassroom() {
        ClassRoomState classState = classRoomStateRepository.getOne(new Long(1));

        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("My test class");
        classRoom.setState(classState);
        classRoomRepository.save(classRoom);

        ClassRoom classRoom2 = classRoomRepository.getOne(classRoom.getId());
        assertEquals(classRoom.getName(), classRoom2.getName());
    }

    @Test
    @Transactional
    public void changeClassRoomStatus() {
        //TODO:: write a classroom first
        Optional<ClassRoomState> state = classRoomStateRepository.findByName("Restricted");

        ClassRoom room = new ClassRoom();
        room.setName("testRoom");
        room.setState(state.get());

//        ClassRoom classRoom = classRoomRepository.getOne(new Long(1));
//        ClassRoomState classState = classRoomStateRepository.getOne(new Long(2));
//
//        classRoom.setState(classState);
        classRoomRepository.save(room);

        ClassRoom classRoom2 = classRoomRepository.findByName("testRoom").get();
        assertEquals(classRoom2.getState().getName(), state.get().getName());
    }

    @Test
    @Transactional
    public void saveSkype() {
        SkypeRoom room = new SkypeRoom("url", "testRoomDB", LocalDateTime.now(), LocalDateTime.now(),"");
        skypeRepo.save(room);
        Optional<SkypeRoom> test = skypeRepo.findByName("testRoomDB");
        assertEquals(room.getName(),test.get().getName());
    }
    
    
    @Test
    @Transactional
    public void saveTeams(){
        Teams team1 = new Teams("team1","url");
        teamsRepo.save(team1);
     
        
        assertEquals(teamsRepo.findAll().size(),2);
        assertEquals(teamsRepo.findAll().get(0).getName(),"team1");
    }

}
