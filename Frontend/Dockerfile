FROM node:18-alpine

WORKDIR /app

# Install pnpm
RUN npm install -g pnpm

# Copy dependency definitions
COPY package.json pnpm-lock.yaml* ./

# Install dependencies (fast and determinstic via lockfile)
RUN pnpm install --frozen-lockfile

# Copy all source files
COPY . .

# Build args
ARG NEXT_PUBLIC_API_BASE
ENV NEXT_PUBLIC_API_BASE=$NEXT_PUBLIC_API_BASE
ENV NEXT_TELEMETRY_DISABLED=1

# Build the application
RUN pnpm build
RUN cp -r public .next/standalone/public
RUN cp -r .next/static .next/standalone/.next/static

# Run the application
EXPOSE 3000
ENV PORT=3000
ENV HOSTNAME="0.0.0.0"

CMD ["node", ".next/standalone/server.js"]
