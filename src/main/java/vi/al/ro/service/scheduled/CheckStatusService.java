package vi.al.ro.service.scheduled;

import javafx.beans.property.StringProperty;

public interface CheckStatusService {

    ThreadLocal<StringProperty> getMessage();
}
