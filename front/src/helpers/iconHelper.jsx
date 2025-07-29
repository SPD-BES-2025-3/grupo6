import { VisibilityOff } from "@mui/icons-material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import DeleteIcon from "@mui/icons-material/Delete";
import ErrorIcon from "@mui/icons-material/Error";
import InfoIcon from "@mui/icons-material/Info";
import UndoIcon from "@mui/icons-material/Undo";
import VisibilityIcon from "@mui/icons-material/Visibility";
import WarningIcon from "@mui/icons-material/Warning";
import { FaCalendar, FaEdit, FaUser } from "react-icons/fa";
import { HiCog, HiHome } from "react-icons/hi";
import { MdCancel, MdLogout, MdOutlineHouse } from "react-icons/md";
import { FaBook } from "react-icons/fa";
import { BsBookmarkCheckFill } from "react-icons/bs";

const iconMap = {
    Book: FaBook,
    Logout: MdLogout,
    User: FaUser,
    Home: HiHome,
    View: VisibilityIcon,
    NotVisible: VisibilityOff,
    Edit: FaEdit,
    Cancel: MdCancel,
    Trash: DeleteIcon,
    Config: HiCog,
    Info: InfoIcon,
    Success: CheckCircleIcon,
    Error: ErrorIcon,
    Warning: WarningIcon,
    Return: UndoIcon,
    Calendar: FaCalendar,
    Welcome: MdOutlineHouse,
    Reservation: BsBookmarkCheckFill,
};

const Icon = ({ name, ...props }) => {
    const DynamicComponent = iconMap[name];
    if (!DynamicComponent) {
        console.warn(`Icone '${name}' não encontrado.`);
        return null;
    }
    return <DynamicComponent {...props} />;
};

export default Icon;
