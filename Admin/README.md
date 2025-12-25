# Beef Restaurant - Admin Dashboard

Admin dashboard for managing Beef Restaurant built with Next.js and React.

## Features

- 📊 **Dashboard** - Overview statistics and quick actions
- 🛒 **Order Management** - View and manage customer orders
- 🍖 **Menu Management** - Full CRUD operations for menu items
- 👥 **User Management** - View and manage registered users
- 🎨 **Modern UI** - Matching Frontend theme with responsive design

## Tech Stack

- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Forms**: React Hook Form
- **Icons**: Lucide React

## Getting Started

### Prerequisites

- Node.js 18+ installed
- Backend services running (Gateway, UserService, OrderingService, MenuService)

### Installation

1. Install dependencies:
```bash
npm install
```

2. Create environment file:
```bash
cp .env.example .env.local
```

3. Update `.env.local` with your API gateway URL:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Development

Run the development server:

```bash
npm run dev
```

The admin dashboard will be available at [http://localhost:3001](http://localhost:3001)

### Build

Build for production:

```bash
npm run build
```

Start production server:

```bash
npm start
```

## Project Structure

```
Admin/
├── app/                    # Next.js app directory
│   ├── dashboard/         # Dashboard pages
│   │   ├── orders/       # Order management
│   │   ├── menu/         # Menu management
│   │   └── users/        # User management
│   ├── globals.css       # Global styles
│   ├── layout.tsx        # Root layout
│   └── page.tsx          # Home page
├── components/            # React components
│   ├── layout/           # Layout components
│   ├── menu/             # Menu-specific components
│   └── ui/               # Reusable UI components
├── lib/                   # Utilities and API
│   ├── api/              # API service functions
│   └── utils.ts          # Helper functions
└── types/                 # TypeScript type definitions
```

## API Integration

The dashboard integrates with the following backend services:

- **Orders**: `/api/v1/admin/orders`
- **Menu**: `/api/v1/menu/items`
- **Users**: `/api/v1/users/admin`

All API calls go through the Gateway Service configured in `NEXT_PUBLIC_API_URL`.

## Authentication

> **Note**: Authentication will be implemented in the next commit. Currently, the dashboard is accessible without login for development purposes.

## License

Private - Beef Restaurant
