package org.wolflink.minecraft.wolfird.framework.jpa;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 框架通用玩家数据
 *
 * uuid         玩家的uuid
 * displayName  玩家游戏名称
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "common_player_data")
public class CommonPlayerData {
    @Id
    String uuid;
    @Column(name = "display_name")
    String displayName;

}
