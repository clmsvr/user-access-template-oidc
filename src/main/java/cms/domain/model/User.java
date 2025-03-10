package cms.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "User")
public class User
{
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@NotBlank
	private String oidcId;
	@NotBlank
	private String providerName; 
	
	@NotBlank
    private String email; 
	@NotBlank
    private String name;
	
    private String city;
    private String state;
    
    private int    numBlocksSubtitled;
    private int    numBlocksTranslated;
    private String comment;        //descricao do proprio usuario
    
    private LocalDateTime   creationDate = LocalDateTime.now() ;
    private LocalDateTime   updateDate = LocalDateTime.now() ; 
}
