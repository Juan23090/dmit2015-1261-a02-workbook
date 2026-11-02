package dmit2015.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "HTE_REVINFO")
public class HteRevinfo {
    @Id
    @Column(name = "RN_", nullable = false)
    private Long id;

    @Column(name = "REV")
    private Long rev;

    @Column(name = "REVTSTMP")
    private Long revtstmp;


}