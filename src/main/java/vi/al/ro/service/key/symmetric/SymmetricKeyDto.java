package vi.al.ro.service.key.symmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Key;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymmetricKeyDto implements SymmetricKeyService {

    private Key key;
}
