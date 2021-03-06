package ie.britoj.currencyexchangerates.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.britoj.currencyexchangerates.configurations.WebMvcConfiguration;
import ie.britoj.currencyexchangerates.web.viewmodels.SignUpViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebMvcConfiguration.class})
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class SignUpControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private SignUpController signUpController;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(signUpController)
                .build();

    }

    @Test
    public void testGetShouldReturnSignUpViewModelForm() throws Exception {
        MvcResult result = mockMvc.perform(get("/signup"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = result.getModelAndView();
        assertThat(modelAndView.getViewName()).isEqualTo("signup-form");
        assertThat(modelAndView.getModel().get("signUp").getClass()).isEqualTo(SignUpViewModel.class);
    }

    @Test
    public void testPostWithValidViewModelShouldRedirectToSuccessPage() throws Exception {
        SignUpViewModel signUpViewModel = createValidSignUpViewModel();

        MvcResult result = mockMvc.perform(post("/signup")
                .flashAttr("signUp", signUpViewModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin?userCreated")).
                        andReturn();

    }

    private SignUpViewModel createValidSignUpViewModel(){
        SignUpViewModel signUpViewModel = new SignUpViewModel();
        signUpViewModel.setEmail("some_email@gmail.com");
        signUpViewModel.setPassword("Password@123");
        signUpViewModel.setConfirmPassword("Password@123");
        signUpViewModel.setDateOfBirth(LocalDate.of(1987, 8, 5));
        signUpViewModel.setStreetAddress("Test Street Name");
        signUpViewModel.setCity("Test city name");
        signUpViewModel.setCountry("Test country name");
        signUpViewModel.setZipCode("123456");
        return signUpViewModel;

    }
}