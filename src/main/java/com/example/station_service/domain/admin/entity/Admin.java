package com.example.station_service.domain.admin.entity;

import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Admin extends User {
}
