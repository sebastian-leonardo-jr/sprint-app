package com.sprint.controller;

import com.sprint.domain.Board;
import com.sprint.domain.User;
import com.sprint.repository.BoardRepository;
import com.sprint.repository.NotificationRepository;
import com.sprint.repository.TeamRoleRepository;
import com.sprint.repository.UserRepository;
import com.sprint.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TeamRoleRepository teamRoleRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    // <-- My Board -->
    @GetMapping("/addMyBoard")
    public void addMyBoard(@RequestParam(value = "name") String name, @RequestParam(value = "description") String description, @RequestParam(value = "dod") String dod) {
        User user =  userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Board board = new Board(name, description, dod);
        user.getMyBoards().add(board);
        userRepository.save(user);
    }

    @GetMapping("/getMyBoard")
    public HashMap<String, Object> getMyBoard(@RequestParam(value = "id") Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        HashMap<String, Object> returnMap = new HashMap<>();

        assert board != null;
        returnMap.put("name", board.getName());
        returnMap.put("description", board.getDescription());
        returnMap.put("dod", board.getDefinitionOfDone());

        return returnMap;
    }
    // <-- !My Board -->

    @GetMapping("/delete")
    public void delete(@RequestParam(value = "id") Long id) {
        boardRepository.deleteById(id);
    }

    @GetMapping("/save")
    public void save(@RequestParam(value = "id") Long id, @RequestParam(value = "name") String name, @RequestParam(value = "description") String description, @RequestParam(value = "dod") String dod) {
        Board b = boardRepository.findById(id).orElse(null);
        assert b != null;
        b.setName(name);
        b.setDescription(description);
        b.setDefinitionOfDone(dod);
        boardRepository.save(b);
    }

    @GetMapping("/users/search")
    public String searchUser(@RequestParam(value = "email") String email) {
        if (userRepository.existsByEmail(email)) {
            return email;
        }
        else {
            return "fail";
        }
    }

//    @GetMapping("/users/invite")
//    public String inviteUser(@RequestParam(value = "id") Long id, @RequestParam("email") String email) {
//        User user = userRepository.findByEmail(email);
//        Board board = boardRepository.findById(id).orElse(null);
//        assert board != null;
//        notificationService.boardInvitation(user, board);
//        return "success";
//    }

//    @GetMapping("/accept")
//    public String acceptInvitation(@RequestParam(value = "id") Long id) {
//        Notification notification = notificationRepository.findById(id).orElse(null);
//        assert notification != null;
//        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        Board board = notification.getBoard();
//        TeamRole boardRole = new TeamRole(user, board, "Developer"); // Defaults to non-admin role
//        user.getBoardRoles().add(boardRole);
//        board.getBoardRoles().add(boardRole);
//        boardRoleRepository.save(boardRole);
//        notificationService.delete(id);
//        return "success";
//    }

//    @GetMapping("/decline")
//    public String declineInvitation(@RequestParam("id") Long id) {
//        notificationService.delete(id);
//        return "success";
//    }
}