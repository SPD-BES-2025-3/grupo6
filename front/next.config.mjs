/** @type {import('next').NextConfig} */
const nextConfig = {
    output: "export",
    reactStrictMode: true,
    trailingSlash: true,
    productionBrowserSourceMaps: false,
    images: { unoptimized: true },
    eslint: { ignoreDuringBuilds: true },
    env: {
        BASE_API_URL: process.env.BASE_API_URL,
    },
};

export default nextConfig;
