package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.GroupsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.StudentsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;

/**
 * Implementation of service interface for managing groups.
 */
@Log4j2
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupsRepository repository;

	@Autowired
	private StudentsRepository studentRepository;

	@Override
	public Group saveNewGroup(Group group) {
		if (group == null) {
			log.info("Can't save - group is NULL");
			return null;
		}
		log.info("Start saving new group: " + group.toString());

		return repository.save(group);
	}

	@Override
	public List<Group> saveAllGroups(List<Group> groups) {
		if (groups == null) {
			log.info("Can't save - groups is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s groups:", groups.size()));

		List<Group> savedGroups = repository.saveAll(groups);

		log.info(savedGroups.size() + " new groups was saved");
		return savedGroups;

	}

	@Override
	public Optional<Group> getGroup(Long id) {
		log.info("Start searching of Group by id=" + id);
		return repository.findById(id);
	}

	@Override
	public List<Group> getAll() {
		log.info("Start research of All Groups");
		return repository.findAll();
	}

	@Override
	public Group updateGroup(Group group) {
		if (group == null) {
			log.info("Can't update - group is NULL");
			return null;
		}
		log.info("Starting update " + group.toString());
		return repository.save(group);
	}

	@Override
	public void deleteGroup(Group group) {
		if (group == null) {
			log.info("Can't delete - group is NULL");
			return;
		}
		log.info("Start deleting group: " + group.toString());

		repository.delete(group);
	}

	@Override
	public void deleteListOfGroups(List<Group> groups) {
		if (groups == null) {
			log.info("Can't delete - list of groups is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s groups", groups.size()));

		repository.deleteAll(groups);
	}

	@Override
	public long countGroups() {
		log.info("Counting groups");
		return repository.count();
	}

	@Override
	public boolean isGroupNameUnique(String groupName) {
		log.info("Checking is unique groupName=" + groupName);
		Optional<Group> group = repository.findByGroupNameContaining(groupName);

		return !group.isPresent();
	}

	@Override
	public boolean setNewGroupName(Long groupId, String groupName) {
		log.info("Start set new GrupName to Group where id=" + groupId);

		if (isGroupNameUnique(groupName)) {
			Optional<Group> grOptional = getGroup(groupId);
			if (grOptional.isPresent()) {
				Group group = grOptional.get();
				group.setGroupName(groupName);
				updateGroup(group);
				return true;
			}
		}
		return false;
	}

	@Override
	public Group addStudentsToGroup(Long groupId, List<Long> selectedStudents) {
		log.info("Start add selected Students to Group where id=" + groupId);

		List<Student> students = studentRepository.findAllById(selectedStudents);
		Optional<Group> grOptional = getGroup(groupId);
		if (grOptional.isPresent()) {
			Group group = grOptional.get();
			if (group.getStudents() == null) {
				group.setStudents(new HashSet<>());
			}
			for (Student student : students) {
				student.setGroup(group);
				group.getStudents().add(student);
			}

			return updateGroup(group);
		}
		return null;
	}

	@Override
	public Group removeStudentsFromGroup(Long groupId, List<Long> selectedStudents) {
		log.info("Start remove selected Students from Group where id=" + groupId);

		List<Student> students = studentRepository.findAllById(selectedStudents);
		Optional<Group> grOptional = getGroup(groupId);
		if (grOptional.isPresent()) {
			Group group = grOptional.get();
			if (group.getStudents() == null) {
				group.setStudents(new HashSet<>());
			}
			for (Student student : students) {
				if (group.getStudents().contains(student)) {
					student.setGroup(null);
					group.getStudents().remove(student);
				}
			}

			return updateGroup(group);
		}
		return null;
	}

	@Override
	public boolean editStudentsForThatGroup(Long groupId, Long[] selectedStudents, String action) {
		log.info("Start edit selected Students for Group where id=" + groupId);

		Optional<Group> groupOptional = getGroup(groupId);
		Group group = null;
		if (groupOptional.isPresent()) {
			if (action.equals("add")) {
				group = addStudentsToGroup(groupId, Arrays.asList(selectedStudents));

			}
			if (action.equals("remove")) {
				group = removeStudentsFromGroup(groupId, Arrays.asList(selectedStudents));

			}
		}
		return group != null;
	}

	@Override
	public List<Group> getGroupsByIds(List<Long> groupsIds) {
		log.info("Search groups by ids");
		return repository.findAllById(groupsIds);
	}

}
