package ua.od.hillel.todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.od.hillel.todo.dao.TODODao;
import ua.od.hillel.todo.entities.TODOEntry;
import ua.od.hillel.todo.entities.TODOList;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

/**
 * Base controller
 */
@Controller
@RequestMapping("/")
public class TODOListController {


    private static final Logger logger = Logger.getLogger(TODOListController.class);

    /**
     * Dao
     */
    @Autowired
    private TODODao dao;

    /**
     * List lists
     */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(ModelMap model) {
        logger.debug(model);
        model.addAttribute("lists", dao.findTODOLists());
		return "index";
	}

    /**
     * Sort lists
     */
    @RequestMapping(value = "/lists/sort/", method = RequestMethod.GET)
    public String sort(ModelMap model) {
        model.addAttribute("lists", dao.sortTODOLists());
        return "index";
    }

    /**
     * Delete
     */
    @RequestMapping(value = "/lists/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, ModelMap model) {
        dao.delete(id);
        return "redirect:/";
    }

    /**
     * Show list
     */
    @RequestMapping(value = "/lists/{id}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable Long id, ModelMap model) {
        TODOList list = dao.load(id);
        model.addAttribute("list", list);

        TODOEntry entry = new TODOEntry();
        entry.setList(list);
        return new ModelAndView("lists/show", "command", entry);
    }

    /**
     * Create List
     */

    @RequestMapping(value = "/lists/addlist", method = RequestMethod.POST)
    public String addList(@ModelAttribute("list") TODOList todoList, BindingResult result) {
        dao.create(todoList);
        return "redirect:/";
    }
    @RequestMapping("/lists/create")
    public ModelAndView showForm() {
        return new ModelAndView("list", "command", new TODOList());
    }


    @RequestMapping(value="/entries/new", method = RequestMethod.POST)
    public String newEntry(@ModelAttribute("entry") TODOEntry entry) {
        dao.create(entry);
        return "redirect:/lists/" + entry.getList().getId();
    }

    /**
     * About
     */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about() {
		return "about";
	}

}
