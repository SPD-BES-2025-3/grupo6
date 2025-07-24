import InsertCommentIcon from "@mui/icons-material/InsertComment";
import EmailIcon from "@mui/icons-material/Email";
import { VisibilityOff } from "@mui/icons-material";
import DownloadIcon from "@mui/icons-material/Download";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import AssignmentIndIcon from "@mui/icons-material/AssignmentInd";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import DeleteIcon from "@mui/icons-material/Delete";
import ErrorIcon from "@mui/icons-material/Error";
import FactCheckIcon from "@mui/icons-material/FactCheck";
import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import GppGoodIcon from "@mui/icons-material/GppGood";
import GroupWorkIcon from "@mui/icons-material/GroupWork";
import HideSourceIcon from "@mui/icons-material/HideSource";
import InfoIcon from "@mui/icons-material/Info";
import LocalShippingIcon from "@mui/icons-material/LocalShipping";
import MapIcon from "@mui/icons-material/Map";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import PaymentsIcon from "@mui/icons-material/Payments";
import ReadMoreIcon from "@mui/icons-material/ReadMore";
import ReceiptIcon from "@mui/icons-material/Receipt";
import ReportIcon from "@mui/icons-material/Report";
import RestartAltIcon from "@mui/icons-material/RestartAlt";
import SearchIcon from "@mui/icons-material/Search";
import TableRowsIcon from "@mui/icons-material/TableRows";
import UndoIcon from "@mui/icons-material/Undo";
import UnfoldMoreIcon from "@mui/icons-material/UnfoldMore";
import ViewColumnIcon from "@mui/icons-material/ViewColumn";
import VisibilityIcon from "@mui/icons-material/Visibility";
import WarningIcon from "@mui/icons-material/Warning";
import YoutubeSearchedForIcon from "@mui/icons-material/YoutubeSearchedFor";

import { AiFillAlert, AiFillBank } from "react-icons/ai";
import { BiPackage } from "react-icons/bi";
import { BsClipboardPlus, BsGraphUp } from "react-icons/bs";
import { FaCalendar, FaCopy, FaEdit, FaFile, FaFileSignature, FaImage, FaList, FaPhone, FaRegCopy, FaRegFileExcel, FaRegFileWord, FaRegSave, FaUser } from "react-icons/fa";
import { FaArrowPointer, FaArrowsSpin, FaFilter, FaFilterCircleXmark, FaMoneyCheckDollar, FaRegFilePdf, FaUserPlus } from "react-icons/fa6";
import { GoLaw } from "react-icons/go";
import { GrValidate } from "react-icons/gr";
import { HiCog, HiHome, HiOutlineViewGrid, HiOutlineViewList } from "react-icons/hi";
import { IoMdArrowBack, IoMdArrowDown, IoMdArrowForward, IoMdArrowUp, IoMdCheckmarkCircleOutline, IoMdLock, IoMdNotifications, IoMdUnlock } from "react-icons/io";
import { IoCheckboxOutline, IoChevronBack, IoChevronDown, IoChevronForward, IoChevronUp, IoCodeSlash, IoFileTrayFull, IoFolder, IoMenu, IoReturnUpBack } from "react-icons/io5";
import { LiaFileInvoiceSolid, LiaHistorySolid } from "react-icons/lia";
import { MdAddCircle, MdCancel, MdDarkMode, MdFavorite, MdFirstPage, MdHelp, MdLastPage, MdLightMode, MdLogout, MdOpenInNew, MdOutlineChair, MdOutlineHouse, MdOutlineScreenshotMonitor, MdShoppingCart } from "react-icons/md";
import { RiChatFollowUpFill } from "react-icons/ri";
import { TbClockPlay, TbRefresh, TbRefreshAlert, TbSpeakerphone } from "react-icons/tb";

import LanguageIcon from "@mui/icons-material/Language";
import { GrGallery } from "react-icons/gr";
import { FaStar } from "react-icons/fa";
import { FaRegStar } from "react-icons/fa";
import { MdOutlineZoomIn } from "react-icons/md";
import { MdOutlineZoomOut } from "react-icons/md";
import { FaBook } from "react-icons/fa";

const iconMap = {
    Book: FaBook,
    ZoomOut: MdOutlineZoomOut,
    ZoomIn: MdOutlineZoomIn,
    ViewGrid: HiOutlineViewGrid,
    ViewList: HiOutlineViewList,
    Notifications: IoMdNotifications,
    DarkMode: MdDarkMode,
    LightMode: MdLightMode,
    Logout: MdLogout,
    Filter: FaFilter,
    FilterCancel: FaFilterCircleXmark,
    Alert: AiFillAlert,
    ClipboardPlus: BsClipboardPlus,
    User: FaUser,
    Unlock: IoMdUnlock,
    Fragment: IoCodeSlash, // </>
    ShoppingCart: MdShoppingCart,
    Next: IoChevronForward, // >
    Back: IoChevronBack, // <
    Down: IoChevronDown, // v
    Up: IoChevronUp, // ^
    Folder: IoFolder,
    Menu: IoMenu,
    ScreenshotMonitor: MdOutlineScreenshotMonitor,
    Home: HiHome,
    View: VisibilityIcon,
    Edit: FaEdit,
    Lock: IoMdLock,
    Cancel: MdCancel,
    Trash: DeleteIcon,
    Info: InfoIcon,
    Search: SearchIcon,
    Columns: ViewColumnIcon,
    Rows: TableRowsIcon,
    Help: MdHelp,
    ArrowBack: IoMdArrowBack,
    ArrowNext: IoMdArrowForward,
    ArrowDown: IoMdArrowDown,
    ArrowUp: IoMdArrowUp,
    FirstPage: MdFirstPage,
    LastPage: MdLastPage,
    BackPage: IoReturnUpBack,
    Copy: FaRegCopy,
    Order: LiaFileInvoiceSolid,
    Rubric: FaList,
    History: LiaHistorySolid,
    OpenInNewTab: MdOpenInNew,
    FilesCentral: IoFileTrayFull,
    Budget: FaMoneyCheckDollar,
    PriceMap: MapIcon,
    SupplyOrder: LocalShippingIcon,
    Payment: PaymentsIcon,
    Forwardness: TbClockPlay,
    Certify: GppGoodIcon,
    Contract: FaFile,
    Juridical: GoLaw,
    LoanTerm: FactCheckIcon,
    Config: HiCog,
    Requester: AssignmentIndIcon,
    Success: CheckCircleIcon,
    Error: ErrorIcon,
    Warning: WarningIcon,
    Supplier: BiPackage,
    Pdf: FaRegFilePdf,
    Excel: FaRegFileExcel,
    Docx: FaRegFileWord,
    Image: FaImage,
    File: AttachFileIcon,
    DefaultAttach: AttachFileIcon,
    Flow: FaArrowsSpin,
    BuyerJustification: ReadMoreIcon,
    FiscalNote: ReceiptIcon,
    Launches: FormatListBulletedIcon,
    Reorder: UnfoldMoreIcon,
    ChangeUrgency: TbRefreshAlert,
    Hide: HideSourceIcon,
    Show: YoutubeSearchedForIcon,
    Priority: ReportIcon,
    Resource: GroupWorkIcon,
    Add: MdAddCircle,
    Reset: RestartAltIcon,
    MoreVert: MoreVertIcon,
    Return: UndoIcon,
    Comment: InsertCommentIcon,
    Email: EmailIcon,
    Bulletin: TbSpeakerphone,
    Goods: MdOutlineChair,
    Report: BsGraphUp,
    Checkable: IoCheckboxOutline,
    AddUser: FaUserPlus,
    ReturnSol: TbRefresh,
    FollowUp: RiChatFollowUpFill,
    Calendar: FaCalendar,
    Welcome: MdOutlineHouse,
    Copy: FaCopy,
    Pointer: FaArrowPointer,
    Download: DownloadIcon,
    Phone: FaPhone,
    Bank: AiFillBank,
    Check: IoMdCheckmarkCircleOutline,
    Justification: GrValidate,
    Signature: FaFileSignature,
    NotVisible: VisibilityOff,
    Favorite: MdFavorite,
    Gallery: GrGallery,
    Site: LanguageIcon,
    Save: FaRegSave,
    Favorite: FaStar,
    NotFavorite: FaRegStar,
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
