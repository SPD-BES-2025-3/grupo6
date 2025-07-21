import React from "react";

export default function NextButton({ type = "button", text }) {
    const [isHovered, setIsHovered] = React.useState(false);

    return (
        <button
            id={"buttonLogin"}
            type={type}
            style={{
                position: "relative",
                display: "inline-flex",
                width: "100%",
                alignItems: "center",
                justifyContent: "center",
                padding: "12px 48px",
                overflow: "hidden",
                fontSize: "18px",
                fontWeight: "500",
                color: isHovered ? "#000000" : "#fff",
                border: "2px solid #fff",
                borderRadius: "9999px",
                backgroundColor: isHovered ? "#f9fafb" : "transparent",
                cursor: "pointer",
                transition: "color 0.3s ease, background-color 0.3s ease",
            }}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <span
                style={{
                    position: "absolute",
                    left: 0,
                    width: "100%",
                    height: isHovered ? "100%" : "0",
                    // backgroundColor: "#3944ff",
                    top: isHovered ? "0" : "50%",
                    transition: "all 0.4s ease",
                    opacity: 1,
                }}
            ></span>
            <span
                style={{
                    position: "absolute",
                    right: 0,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "flex-start",
                    width: "40px",
                    height: "40px",
                    transform: isHovered ? "translateX(0)" : "translateX(100%)",
                    transition: "transform 0.3s ease",
                }}
            >
                <svg style={{ width: "20px", height: "20px" }} fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
                </svg>
            </span>
            <span style={{ position: "relative" }}>{text}</span>
        </button>
    );
}
