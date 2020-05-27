package sa775.Sprint.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sa775.Sprint.Domain.*;
import sa775.Sprint.Repository.BoardRepository;
import sa775.Sprint.Repository.TeamRoleRepository;
import sa775.Sprint.Repository.UserRepository;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRoleRepository boardRoleRepository;
    @Autowired
    private BoardRepository boardRepository;

    // For debugging
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("users") User user) {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user =  userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.getMyBoards().sort(Comparator.comparing(Board::getCreated));

        model.addAttribute("user", user);

        // Notifications
        List<Notification> notifications = new ArrayList<>(user.getNotifications());
        notifications.sort(Comparator.comparing(Notification::getDatetime).reversed());
        //Length
        int notificationsLength = 0;
        for (Notification notification : user.getNotifications()) {
            if (!notification.isSeen()) {
                notificationsLength++;
            }
        }
        model.addAttribute("notifications", notifications);
        model.addAttribute("notificationsLength", notificationsLength);

        return "dashboard";
    }

    @GetMapping("/tasks")
    public String tasks(Model model, @RequestParam(value = "id") Long id) {
        User user =  userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setCurrentBoardId(id);
        userRepository.save(user);

        // Notifications
        List<Notification> notifications = new ArrayList<>(user.getNotifications());
        notifications.sort(Comparator.comparing(Notification::getDatetime).reversed());
        //Length
        int notificationsLength = 0;
        for (Notification notification : user.getNotifications()) {
            if (!notification.isSeen()) {
                notificationsLength++;
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("board", boardRepository.findById(id));
        model.addAttribute("notifications", notifications);
        model.addAttribute("notificationsLength", notificationsLength);

        return "tasks";
    }

    @GetMapping("/account")
    public String account(Model model) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("user", user);

        // Notifications
        List<Notification> notifications = new ArrayList<>(user.getNotifications());
        notifications.sort(Comparator.comparing(Notification::getDatetime).reversed());
        //Length
        int notificationsLength = 0;
        for (Notification notification : user.getNotifications()) {
            if (!notification.isSeen()) {
                notificationsLength++;
            }
        }

        model.addAttribute("notifications", notifications);
        model.addAttribute("notificationsLength", notificationsLength);

        return "account";
    }
}