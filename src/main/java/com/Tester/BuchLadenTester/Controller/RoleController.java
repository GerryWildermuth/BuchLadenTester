package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Repository.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.Optional;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller
public class RoleController {

    final
    RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @DeleteMapping(value = "/Role")
    public String DeleteRole(@Valid int RoleId, BindingResult bindingResult, ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Role> role = roleRepository.findById(RoleId);
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(role==null){
            modelAndView.addObject("successMessage", "There is no Role with this RoleId");
            logger.info("There is no Role with this RoleId");
        }
        // we will save the book if, no binding errors
        else {
            roleRepository.deleteById(RoleId);
            modelAndView.addObject("successMessage", "Role with RoleId "+RoleId+" got removed!");
            logger.info("Role with RoleId"+RoleId+" got removed!");
        }
        String successMessage = "Role with RoleId" + RoleId + " got removed!";
        return successMessage;
    }
}
