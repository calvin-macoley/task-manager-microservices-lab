package task_manager_microservices_lab.task_service.model;

import java.sql.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import task_manager_microservices_lab.task_service.enumeration.TaskStatusEnum;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "user_id")
    private Long userId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
    private String description;
	
	@CreationTimestamp
	private Instant createdOn;
	
	@UpdateTimestamp
	private Timestamp updateAt;
    
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;
}
