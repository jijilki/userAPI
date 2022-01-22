package com.neo.userAPI;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@RestController
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//Added MongoAuditing so that database table have time field gets updated automatically
@EnableMongoAuditing
public class UserApiApplication extends WebSecurityConfigurerAdapter implements CommandLineRunner {

    @Autowired
    SignInUserInt signInUserInterface;


    @Autowired
    TodoInterface todoInterface;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    KafkaTemplate<String,TodoEntity> kafkaTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

   /* @Autowired
    UserDetailsService userDetailsService;*/

    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        userRepository.deleteAll();
        userRepository.save(new UserEntity(1122L, "Neo", passwordEncoder.encode("Meow"), "READ_ACCESS,WRITE_ACCESS,ADMIN_ACCESS"));
        userRepository.save(new UserEntity(1123L, "Leo", passwordEncoder.encode("Woof"), "READ_ACCESS"));

    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> authenticate(@RequestBody UserRequest userRequest) {
        System.out.println(userRequest.getUserName() + " :: Attempting login");
        UserResponse userResponse = signInUserInterface.validateUser(userRequest);
        System.out.println(userResponse);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }


    @RequestMapping(value = "/addTodo", method = RequestMethod.POST)
    public ResponseEntity createTodoTask(@RequestBody TodoEntity todoRequest) {
        ResponseEntity resp;
        if (TodoUtil.hasWriteAccess()) {
            List<TodoEntity> todo = todoInterface.createTodo(todoRequest);
            resp = new ResponseEntity<>(todo, HttpStatus.OK);
            sendMessageToKafkaQueue(todoRequest);
        } else {

            resp = new ResponseEntity<>("No Write access for the user", HttpStatus.UNAUTHORIZED);

        }
        return resp;

    }

    @RequestMapping(value = "getTodoList" , method = RequestMethod.GET)
    public ResponseEntity getTodoTasks () {
        if (TodoUtil.hasReadAccess()) {
            Long userId = JwtTokenUtil.getUserIdFromSecurityContext();
            List<TodoEntity> todoEntities = todoInterface.getTodoTaskList(userId);
            return  new ResponseEntity(todoEntities, HttpStatus.OK);

        }
        return  new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

    }

    @RequestMapping(value = "updateTodoTask" ,method = RequestMethod.POST)
    public ResponseEntity updateTodoTask(@RequestBody TodoEntity todoRequest){


        if(TodoUtil.hasWriteAccess()){
            try {
                List<TodoEntity> todoEntities = todoInterface.updateTodoTask(todoRequest);
                return new ResponseEntity<>(todoEntities, HttpStatus.OK);
            }
            catch (IOException e){
                return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return  new ResponseEntity("Authorization missing" ,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "deleteTodo" ,method = RequestMethod.DELETE)
    public ResponseEntity deleteTodoTask(@RequestBody TodoEntity todoEntity){

        todoInterface.deleteTodo(todoEntity);

        return new ResponseEntity( "Deleted Todo", HttpStatus.OK);
    }

    // Controller method for Kafka Consumer


   // @KafkaListener(topics = "TutorialTopic", groupId = "groupId")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }


    public void sendMessageToKafkaQueue(TodoEntity todoReq){
      // kafkaTemplate.send("TutorialTopic",todoReq);
    }


    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
            "/authenticate"
    };
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                    .disable().
                // dont authenticate this particular request
                        authorizeRequests().antMatchers(AUTH_WHITELIST)
                .permitAll().
                // all other requests need to be authenticated
                        anyRequest().authenticated()
                .and().
                // make sure we use stateless session; session won't be used to
                // store user's state.
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
 }


  /*  @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }*/



    //For encrypted password with hashing and salting
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }



  /*  @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }*/

}
